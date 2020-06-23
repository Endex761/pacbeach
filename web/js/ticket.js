//Set tomorrow as first selectable data
function setMinData(inp) {
    var today = new Date();
    var d = today.getDate();
    var m = today.getMonth()+1; //Inizia da 0
    var y = today.getFullYear();
    
    if (d < 10) {
        d = '0' + d;
    } 
    
    if (m < 10) {
        m = '0' + m;
    } 

    var tomorrow = y + '-' + m + '-' + d;
    inp.setAttribute("min", tomorrow);
}

//Unlock custom time selector if the relative radio is checked
function unlockCustom() {
    if(document.getElementById('customTime').checked) {
        document.getElementById('startTime').disabled = false;
        document.getElementById('endTime').disabled = false;
    } else {
        document.getElementById('startTime').disabled = true;
        document.getElementById('endTime').disabled = true;
    }
}

//Set min end time selector as (start time + 1h)
function setMinEnd() {
    var startInput = document.getElementById('startTime').value;
    var minEndHoursI = parseInt(startInput.substr(0,2)) + 1;
    var minEndHours = minEndHoursI.toString();
    var minEnd = minEndHours + ':00';

    if(minEndHoursI < 10) {
        minEnd = '0' + minEnd;
    }

    document.getElementById('endTime').setAttribute("min", minEnd);
}

//Add another guest field when the button is clicked
var guestCounter = 2;
function addGuest() {
    if(document.getElementById("guest1").value != "") {
        event.preventDefault();

        var i = document.createElement("input");
        i.setAttribute("class", "form-control mt-1");
        i.setAttribute("id", "guest" + guestCounter.toString());
        i.setAttribute("name", "guest" + guestCounter.toString());
        i.setAttribute("placeholder", "Ospite" + guestCounter.toString());
        i.setAttribute("pattern", "^[\w'\-,.][^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{2,45}$");

        var element = document.getElementById("guests");
        element.appendChild(i);

        guestCounter++;
    }
}

//Submit date/time form and keep the value for the booking POST
var start;
var end;

$(function() {
    var form = $('#dt');
    var formMessages = $('#dtconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        //Checking and formatting date/time value
        start = $('#date').val() + "T";
        end = start;

        if($('#day').is(':checked')) {
            start += "08:00:00";
            end += "20:00:00";
        } else if($('#morning').is(':checked')) {
            start += "08:00:00";
            end += "14:00:00";
        } else if($('#afternoon').is(':checked')) {
            start += "14:00:00";
            end += "20:00:00";
        } else if($('#customTime').is(':checked')) {
            start += ($('#startTime').val() + ":00");
            end += ($('#endTime').val() + ":00");
        }

        var formData = "orarioInizio=" + start + "&orarioFine=" + end;

        $.ajax({
            type: 'GET',
            url: $(form).attr('action'),
            data: formData,
            dataType: 'text xml',
            success: function(xml) {
                xml = $(xml);
                var succ = xml.find('success').text();
                var message = xml.find('message').text();
                var content = xml.find('content');

                if(succ == "true") {
                    var element = content.find('element').each(function() {
                        var ombrellone = '#' + $(this).find('idOmbrellone').text();

                        if($(this).find('prenotabile').text() == "true") {
                            $(ombrellone).removeAttr('disabled');
                        } else {
                            $(ombrellone).attr('disabled', 'true');
                            $(ombrellone).removeAttr('class');
                            $(ombrellone).attr('class', 'btn btn-danger');
                        }
                    });
                } else {
                    $(formMessages).attr('style', 'color: red');
                    $(formMessages).text(message);
                }
            },
            error: function(data) {
                alert('Oops! Errore inaspettato!');
            }
        });
    });
})

//Select seats and save the codes in an array
var seats = [];

function selectSeat(b) {
    if(b.getAttribute("class") == "btn btn-success") {
        b.setAttribute("class", "btn btn-outline-success");

        seats.push(b.getAttribute("value"));
    } else {
        b.setAttribute("class", "btn btn-success");

        var i = seats.indexOf(b.getAttribute("value"));
        seats.splice(i, 1);
    }
}

//Submit booking form
$(function() {
    var form = $('#ticket');
    var formMessages = $('#bookingconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();
        
        if(seats.length == 0) {
            $(formMessages).attr('style', 'color: red');
            $(formMessages).text('Seleziona almeno un ombrellone per procedere!');
        } else {

            //Check if paid
            var paid;

            if(($('#pay').data('bs.modal') || {})._isShown) {
                paid = "true";
            } else {
                paid = "false";
            }

            //Create an array with all the guests
            var i;
            var guests = [];
            for (i = 1; i < guestCounter; ++i) {
                if($('#guest' + i).val() != "") {
                    guests.push($('#guest' + i).val());
                }
            }

            var formData = "orarioInizio=" + start + "&orarioFine=" + end + "&pagata=" + paid + "&idOmbrellone=" + seats.toString() + "&ospiti=" + guests.toString();
    
            $.ajax({
                type: 'POST',
                url: $(form).attr('action'),
                data: formData,
                dataType: 'text xml',
                success: function(xml) {
                    xml = $(xml);
                    var succ = xml.find('success').text();
                    var message = xml.find('message').text();

                    if(paid == "true") {
                        $('#pay').modal('toggle');
                    }

                    if(succ == "true") {
                        $(formMessages).attr('style', 'color: green');
                        $(formMessages).text(message);
                    } else {
                        $(formMessages).attr('style', 'color: red');
                        $(formMessages).text(message);
                    }

                    $('#successNotify').modal('toggle');
                },
                error: function(data) {
                    alert('Oops! Errore inaspettato!');
                }
            });
        }
    });
})