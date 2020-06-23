$(document).ready(refreshBar())

function refreshBar() {
    //Get all the orders
    var formMessages = $('#confmessage');

    $.ajax({
        type: 'GET',
        url: './api/barista',
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
                    var idOrder = $(this).find('idOrdine').text();
                    var price = $(this).find('costo').text();
                    var delivery = $(this).find('consegna').text();
                    var status = $(this).find('stato').text();

                    if(delivery == "true") {
                        delivery = "Consegna";
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

                    $('#tableBody').append('<tr><td>' + idOrder + '</td><td>€' + price + '</td><td>' + delivery + '</td><td>' + status + '</td><td><button class="btn btn-info" type="button" onclick="openStatusModal(' + idOrder + ')">Modifica stato</button></td></tr>');
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

//Open the modal to change the status of an order and pass the order ID
function openStatusModal(idToSend) {
    $('#updateStatusModal').modal('toggle');
    $('#idOrdineModifica').val(idToSend);
}

//Update the order status
$(function() {
    var form = $('#upOrder');
    var formMessages = $('#uconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        var status;

        if($('#ready').is(':checked')) {
            status = "B";
        } else if($('#delivering').is(':checked')) {
            status = "C";
        } else if($('#delivered').is(':checked')) {
            status = "D";
        }

        var formData = "idOrdine=" + $('#idOrdineModifica').val() + "&stato=" + status;

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
                } else {
                    $(formMessages).attr('style', 'color: red');
                    $(formMessages).text(message);
                }

                refreshBar();
            },
            error: function(data) {
                alert('Oops! Errore inaspettato!');
            }
        });
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