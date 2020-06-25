//Set the current day as first selectable data
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

    var t = y + '-' + m + '-' + d;
    inp.setAttribute("min", t);
}

//If the user select the current day, set the min time value as the current time
function setMinTime(inp) {
    $('#confDTButton').removeAttr('disabled');
    var today = new Date();

    if($(inp).val() == $(inp).attr('min')) {
        var now = today.getHours();
        var changeTime = true;

        //Disable fixed slots if the booking time exceed the starting time
        if(now >= 8 && now < 14) { //Booking between 8:00 (incl) and 14:00 (excl)
            $('#day').attr('disabled', 'true');
            $('#morning').attr('disabled', 'true');
        } else if(now >= 14 && now < 19) { //Booking between 14:00 (incl) and 19:00 (excl)
            $('#day').attr('disabled', 'true');
            $('#morning').attr('disabled', 'true');
            $('#afternoon').attr('disabled', 'true');
        } else if(now >= 19) { //Booking after 19:00 (incl)
            $('#day').attr('disabled', 'true');
            $('#morning').attr('disabled', 'true');
            $('#afternoon').attr('disabled', 'true');
            $('#customTime').attr('disabled', 'true');
            $('#confDTButton').attr('disabled', 'true');
            changeTime = false;
        }

        //Update custom time restriction
        if(changeTime) {
            ++now;
            var next = now + 1;

            now.toString();
            next.toString();

            now = now + ":00";
            next = next + ":00";

            if(now < 10) {
                now = '0' + now;
            }

            if(next < 10) {
                next = '0' + next;
            }

            $('#startTime').attr('min', now);
            $('#startTime').attr('value', now);
            $('#endTime').attr('min', next);
            $('#endTime').attr('value', next);
        }
    } else {
        $('#day').removeAttr('disabled');
        $('#morning').removeAttr('disabled');
        $('#afternoon').removeAttr('disabled');
        $('#customTime').removeAttr('disabled');
    }
}

//Unlock custom time selector if the relative radio is checked and set a payment base
var payBase = 0;

function unlockCustom() {
    if($('#day').is(':checked')) {
        payBase = 15;
        $('#startTime').attr('disabled', 'true');
        $('#endTime').attr('disabled', 'true');
    } else if($('#customTime').is(':checked')) {
        payBase = 2;
        $('#startTime').removeAttr('disabled');
        $('#endTime').removeAttr('disabled');
    } else {
        payBase = 8;
        $('#startTime').attr('disabled', 'true');
        $('#endTime').attr('disabled', 'true');
    }
}

//Set min end time selector as (start time + 1h)
function setMinEnd() {
    var startInput = $('#startTime').val();
    var minEndHoursI = parseInt(startInput.substr(0,2)) + 1;
    var minEndHours = minEndHoursI.toString();
    var minEnd = minEndHours + ':00';

    if(minEndHoursI < 10) {
        minEnd = '0' + minEnd;
    }

    $('#endTime').attr('min', minEnd);
}

function setHourPayment() {
    var numHours = parseInt($('#endTime').val().substring(0,2)) - parseInt($('#startTime').val().substring(0,2));
    payBase = 2 * numHours;
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

    $('#tot').text(payBase * seats.length);
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