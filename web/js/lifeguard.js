//Enable all tooltip
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});

//Set tomorrow as first selectable data
function setMinData() {
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
    document.getElementById("date").setAttribute("min", tomorrow);
}

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

//Get booking info for today and save them
var booking = [];

$(function() {
    var form = $('#dt');
    var formMessages = $('#dtconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        //Formatting date/time value
        var start = $('#date').val() + "T08:00:00";
        var end = $('#date').val() + "T20:00:00";

        var formData = "orarioInizio=" + start + "&orarioFine=" + end;

        $.ajax({
            type: 'GET',
            url: $(form).attr('action'),
            data: formData,
            dataType: 'text xml',
            success: function(xml) {
                xml = $(xml);
                var succ = xml.find('success').text();
                var content = xml.find('content');

                if(succ == "true") {
                    content.find('element').each(function() {
                        var element = $(this);
                        var index = element.find('ombrellone').find('idOmbrellone').text();

                        if(booking[index] == null) {
                            booking[index] = element;
                        } else {
                            booking[index].push(element);
                        }
                    });
                    console.log("Aggiunti tutti gli elementi!");

                    var i;

                    for(i = 0; i < booking.length; ++i) {
                        var nestedArray = booking[i];
                        if(nestedArray != null) {
                            if(nestedArray.length > 1) {
                                console.log("Ordino le prenotazioni dell'ombrellone: "+i);

                                nestedArray.sort((a, b) => (a.find('orarioFine').text() > b.find('orarioFine').text()) ? 1 : -1);
                                /*for(j = 0; j < nestedArray.length; ++j) {
                                    tmp[j] = nestedArray[j].find('orarioFine').text();
                                }
                                tmp.sort();
                                for(j = 0; j < nestedArray.length; ++j) {
                                    tmp1[j] = nestedArray[j].find('orarioFine').text();
                                }*/
                            }
                        }
                    }
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

//Display info
function selectSeat(b) {

    if(b.getAttribute("class") == "btn btn-info") {
        b.setAttribute("class", "btn btn-outline-info");
        $('#info').empty();
    } else {
        //Set button class
        var i;
        for(i = 1; i < 25; ++i) {
            if(i == b.getAttribute("value")) {
                b.setAttribute("class", "btn btn-info");
            } else {
                $('#' + i).attr("class", "btn btn-outline-info");
            }
        }

        //Empty the info list
        $('#info').empty();
        
        //Set the info list
        var index = b.getAttribute("value");
        $('#info').append('<li>Ombrellone ' + index + ': </li>');

        if(booking[index] == null) {
            $('#info').append('<li>Nessuna prenotazione prevista per oggi!</li>');
        } else {
            var dayBooking = booking[index];

            for(i = 0; i < dayBooking.length; ++i) {
                var timeStart = dayBooking[i].find('orarioInizio').text().substring(11,16);
                var timeEnd = dayBooking[i].find('orarioFine').text().substring(11,16);
                var name = dayBooking[i].find('utente').find('nome').text() + " " + dayBooking.find('utente').find('cognome').text();
                var toPrint = name + "   " + timeStart + " - " + timeEnd;
                $('#info').append('<li>' + toPrint + '</li>');
            }
        }
    }
}