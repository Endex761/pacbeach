var tab;

$(document).ready(function() {
    //Enable table sorting
    tab = $('#table').DataTable({
        "dom": '<"container"<"row"<"col text-left"l><"col"f>>>rt<"container"<"row"<"col text-left"i><"col"p>>>',
        "language": {
            "lengthMenu": "Mostra _MENU_ elementi",
            "emptyTable": "Nessuna prenotazione trovata!",
            "search": "Cerca:",
            "zeroRecords": "Nessuna corrispondenza trovata!",
            "info": "Pagina _PAGE_ di _PAGES_",
            "infoEmpty": "Nessuna pagina disponibile",
            "infoFiltered": "(filtered from _MAX_ total records)",
            "paginate": {
                "first":      "Prima",
                "last":       "Ultima",
                "next":       "Successiva",
                "previous":   "Precedente"
            }
        },
        "columns": [
            null,
            null,
            null,
            { "orderable": false },
            { "orderable": false },
            { "orderable": false }
        ]
    });

    //Get today's info
    var form = $('#dt');
    var formMessages = $('#dtconfmessage');

    //Formatting date/time value
    var today = new Date();
    var d = today.getDate();
    var m = today.getMonth()+1; //Start from 0
    var y = today.getFullYear();
    
    if (d < 10) {
        d = '0' + d;
    } 
    
    if (m < 10) {
        m = '0' + m;
    } 

    var t = y + '-' + m + '-' + d;
    $('#bdate').attr('value', t);

    var formData = "orarioInizio=" + t + "T08:00:00&orarioFine=" + t + "T20:00:00";

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

            if(succ) {
                //Fill the array with the new data
                content.find('element').each(function() {
                    var timeStart = $(this).find('orarioInizio').text().substring(12,17);
                    var timeEnd = $(this).find('orarioFine').text().substring(12,17);
                    var name = $(this).find('utente').find('nome').text() + " " + $(this).find('utente').find('cognome').text();
                    var id = $(this).find('idPrenotazione').text();
                    var price = $(this).find('costo').text();
                    var index = parseInt($(this).find('ombrellone').find('idOmbrellone').text());

                    var paid;
                    if($(this).find('pagata').text() == 'true') {
                        paid = '<button class="btn btn-success" type="button"><i class="fas fa-check"></i></button>';
                    } else {
                        paid = '<button class="btn btn-danger" type="button" id="' + id + '" onclick="markAsPaid(this)"><i class="fas fa-times"></i></button>';
                    }

                    var del = '<button class="btn btn-danger" type="button" id="' + id + '" onclick="markToDelete(this)">Elimina</button>';

                    tab.row.add([
                        name,
                        index,
                        timeStart + ' - ' + timeEnd,
                        '€' + price,
                        paid,
                        del
                    ]).draw(false);
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
})

//Set tomorrow as first selectable data
function setMinData(inp) {
    var today = new Date();
    var d = today.getDate() + 1;
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

//Get booking info for the selected day
$(function() {
    var form = $('#dt');
    var formMessages = $('#dtconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        //Formatting date/time value
        var getStart = $('#bdate').val() + "T08:00:00";
        var getEnd = $('#bdate').val() + "T20:00:00";

        var formData = "orarioInizio=" + getStart + "&orarioFine=" + getEnd;

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
    
                if(succ) {
                    //Empty the table
                    $('#tableBody').remove();

                    //Fill the table with the new data
                    content.find('element').each(function() {
                        var timeStart = $(this).find('orarioInizio').text().substring(12,17);
                        var timeEnd = $(this).find('orarioFine').text().substring(12,17);
                        var name = $(this).find('utente').find('nome').text() + " " + $(this).find('utente').find('cognome').text();
                        var id = $(this).find('idPrenotazione').text();
                        var price = $(this).find('costo').text();
                        var index = parseInt($(this).find('ombrellone').find('idOmbrellone').text());
    
                        var paid;
                        if($(this).find('pagata').text() == 'true') {
                            paid = '<button class="btn btn-success" type="button"><i class="fas fa-check"></i></button>';
                        } else {
                            paid = '<button class="btn btn-danger" type="button" id="' + id + '" onclick="markAsPaid(this)"><i class="fas fa-times"></i></button>';
                        }

                        var del = '<button class="btn btn-danger" type="button" id="' + id + '" onclick="markToDelete(this)">Elimina</button>';

                        tab.row.add([
                            name,
                            index,
                            timeStart + ' - ' + timeEnd,
                            '€' + price,
                            paid,
                            del
                        ]).draw(false);
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

//Select booking to mark as paid
var paid = [];

function markAsPaid(b) {
    if(b.getAttribute("class") == "btn btn-danger") {
        b.setAttribute("class", "btn btn-outline-danger");

        paid.push(b.getAttribute("id"));
    } else {
        b.setAttribute("class", "btn btn-danger");

        var i = paid.indexOf(b.getAttribute("id"));
        paid.splice(i, 1);
    }

    //Unable/disable the "confirm payment" button
    if(paid.length > 0) {
        $('#confPay').removeAttr("disabled");
    } else {
        $('#confPay').attr("disabled", "true");
    }
}

//Confirm booking payment
function pay() {
    var formMessages = $('#pconfmessage');
    var formData = "idPrenotazione=" + paid.toString();

    $.ajax({
        type: 'POST',
        url: './api/pagamento',
        data: formData,
        dataType: 'text xml',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();

            if(succ == "true") {
                $(formMessages).attr('style', 'color: green');
                $(formMessages).text(message);
            } else {
                $(formMessages).attr('style', 'color: red');
                $(formMessages).text(message);
            }
        },
        error: function(data) {
            alert('Oops! Errore inaspettato!');
        }
    });
}

//Select booking to delete
var toDelete = [];

function markToDelete(b) {
    if(b.getAttribute("class") == "btn btn-danger") {
        b.setAttribute("class", "btn btn-outline-danger");

        toDelete.push(b.getAttribute("id"));
    } else {
        b.setAttribute("class", "btn btn-danger");

        var i = toDelete.indexOf(b.getAttribute("id"));
        toDelete.splice(i, 1);
    }

    //Unable/disable the "confirm delete" button
    if(toDelete.length > 0) {
        $('#confDel').removeAttr("disabled");
    } else {
        $('#confDel').attr("disabled", "true");
    }
}

//Confirm booking payment
function deleteBooking() {
    var formMessages = $('#pconfmessage');
    var formData = "idPrenotazione=" + toDelete.toString();

    $.ajax({
        type: 'DELETE',
        url: './api/prenotazione&' + formData,
        dataType: 'text xml',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();

            if(succ == "true") {
                $(formMessages).attr('style', 'color: green');
                $(formMessages).text(message);
            } else {
                $(formMessages).attr('style', 'color: red');
                $(formMessages).text(message);
            }
        },
        error: function(data) {
            alert('Oops! Errore inaspettato!');
        }
    });
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
            //Create an array with all the guests
            var i;
            var guests = [];
            for (i = 1; i < guestCounter; ++i) {
                if($('#guest' + i).val() != "") {
                    guests.push($('#guest' + i).val());
                }
            }
            
            var formData = 'nome=' + $('#name').val() + '&cognome=' + $('#surname').val() + '&telefono=' + $('#phone').val() + '&email=' + $('#suemail').val();
            formData = formData + "&orarioInizio=" + start + "&orarioFine=" + end + "&pagata=true" + "&idOmbrellone=" + seats.toString() + "&ospiti=" + guests.toString();
    
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

//Logout
function logout() {
    $.ajax({
        type: 'GET',
        url: './api/logout',
        dataType: 'text xml',
        success: function(xml) {
            window.location.replace('./home.html');
        },
        error: function(data) {
            alert('Oops! Errore inaspettato!');
        }
    });
}