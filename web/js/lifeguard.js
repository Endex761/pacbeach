//Custom Object with the info about a booking
function Element(ts, te, n, i) {
    this.timeStart = ts;
    this.timeEnd = te;
    this.name = n;
};

//Create a bidimensional array with the bookings of a day
//booking[i] is an array of Element with the info about the seat i
var booking = new Array(25);

$(document).ready(function(){
    var form = $('#dt');
    var formMessages = $('#dtconfmessage');

    //Get the info about today
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
    
    $('#date').attr('value', t);

    //Formatting date/time value
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
                //Empty the array
                var i;
                
                for(i = 0; i < booking.length; ++i) {
                    booking[i] = [];
                }

                //Fill the array with the new data
                content.find('element').each(function() {
                    var timeStart = $(this).find('orarioInizio').text().substring(12,17);
                    var timeEnd = $(this).find('orarioFine').text().substring(12,17);
                    var name = $(this).find('utente').find('nome').text() + " " + $(this).find('utente').find('cognome').text();
                    var index = parseInt($(this).find('ombrellone').find('idOmbrellone').text());
        
                    booking[index].push(new Element(timeStart, timeEnd, name));
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

//Get booking info for the selected day and save them
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
                var message = xml.find('message').text();
                var content = xml.find('content');

                if(succ) {
                    //Empty the array
                    var i;
                    
                    for(i = 0; i < booking.length; ++i) {
                        booking[i] = [];
                    }
    
                    //Fill the array with the new data
                    content.find('element').each(function() {
                        var timeStart = $(this).find('orarioInizio').text().substring(12,17);
                        var timeEnd = $(this).find('orarioFine').text().substring(12,17);
                        var name = $(this).find('utente').find('nome').text() + " " + $(this).find('utente').find('cognome').text();
                        var index = parseInt($(this).find('ombrellone').find('idOmbrellone').text());
            
                        booking[index].push(new Element(timeStart, timeEnd, name));
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

//Display info
function selectSeat(b) {

    if(b.getAttribute("class") == "btn btn-info") {
        //Set button class
        b.setAttribute("class", "btn btn-outline-info");

        //Empty the info list
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

        var iBooking = booking[index];

        if(iBooking.length == 0) {
            $('#info').append('<li>Nessuna prenotazione prevista per oggi!</li>');
        } else {
            for(i = 0; i < iBooking.length; ++i) {
                var toPrint = iBooking[i].name + "   " + iBooking[i].timeStart + " - " + iBooking[i].timeEnd;
                $('#info').append('<li>' + toPrint + '</li>');
            }
        }
    }
}