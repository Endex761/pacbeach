$(document).ready(function() {
    //Enable table sorting
    $.fn.dataTable.moment( 'DD-MM-YYYY' );
    var tab = $('#table').DataTable({
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
            { "orderable": false },
            { "orderable": false },
            { "orderable": false },
            { "orderable": false },
            { "orderable": false }
        ]
    });

    //Get all the user bookings
    var formMessages = $('#confmessage');

    $.ajax({
        type: 'GET',
        url: './api/prenotazione',
        dataType: 'text xml',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();
            var content = xml.find('content');

            if(succ) {
                //Fill the table with the new data
                content.find('element').each(function() {
                    var date = new Date($(this).find('orarioInizio').text());
                
                    var d = date.getDate();
                    var m = date.getMonth()+1; //Start from 0
                    var y = date.getFullYear();
                    
                    if (d < 10) {
                        d = '0' + d;
                    } 
                    
                    if (m < 10) {
                        m = '0' + m;
                    } 
                
                    var dateString = d + '-' + m + '-' + y;
                    var timeStart = $(this).find('orarioInizio').text().substring(11,16);
                    var timeEnd = $(this).find('orarioFine').text().substring(11,16);
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
                        dateString,
                        timeStart + ' - ' + timeEnd,
                        index,
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

    $('#orderTableBody').append('<tr><td>ok</td><td>');

    //Get all the orders
    refreshBar()
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
$(function() {
    var form = $('#payForm');
    var formMessages = $('#pconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        var formData = "idPrenotazione=" + paid.toString();

        $.ajax({
            type: 'POST',
            url: $(form).attr('action'),
            data: formData,
            dataType: 'text xml',
            success: function(xml) {
                xml = $(xml);
                var succ = xml.find('success').text();
                var message = xml.find('message').text();

                if(succ == "true") {
                    $(formMessages).attr('style', 'color: green');
                    $(formMessages).text(message);
                    location.reload();
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
        url: './api/prenotazione?' + formData,
        dataType: 'text xml',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();

            if(succ == "true") {
                $(formMessages).attr('style', 'color: green');
                $(formMessages).text(message);
                location.reload();
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

function refreshBar() {
    //Get all the orders
    var formMessages = $('#bconfmessage');

    $.ajax({
        type: 'GET',
        url: './api/ordine',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();
            var content = xml.find('content');

            if(succ) {
                //Empty the table
                var tab = document.getElementById("orderTable");
                for(var i = tab.rows.length - 1; i > 0; i--) {
                    tab.deleteRow(i);
                }

                //Fill the table with the new data
                content.find('element').each(function() {
                    var idOrder = $(this).find('idOrdine').text();
                    var price = $(this).find('costo').text();
                    var delivery = $(this).find('consegna').text();
                    var status = $(this).find('stato').text();

                    var prodotti = new Array();
                    content.find('prodotti').each(function() {
                        var toInsert = $(this).find('quantita').text() + " x " + $(this).find('prodotto').find('nome').text();
                        prodotti.push(toInsert);
                    })

                    if(delivery == "true") {
                        delivery = "Consegna al tuo ombrellone";
                    } else {
                        delivery = "Take away";
                    }

                    switch(status) {
                        case "A":
                            status = "In preparazione";
                            break;
                        case "B":
                            status = "Pronto";
                            break;
                        case "C":
                            status = "In consegna";
                            break;
                        case "D":
                            status = "Consegnato";
                            break;
                        default:
                            status = "Sconosciuto";
                            break;
                    }

                    $('#orderTable').append('<tr><td>' + idOrder + '</td><td>' + prodotti.toString() + '</td><td>€' + price + '</td><td>' + delivery + '</td><td>' + status + '</td></tr>');
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
}

//Logout
function logout() {
    $.ajax({
        type: 'GET',
        url: './api/logout',
        dataType: 'text xml',
        success: function(xml) {
            window.location.replace('./');
        },
        error: function(data) {
            alert('Oops! Errore inaspettato!');
        }
    });
}