$(document).ready(function() {
    var formMessages = $('#confmessage');

    //Load the menù
    $.ajax({
        type: 'GET',
        url: './api/prodotto',
        dataType: 'text xml',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();
            var content = xml.find('content');

            if(succ) {
                var counter = 0;
                var rowNum = 0;

                //Fill the array with the new data
                content.find('element').each(function() {
                    var name = $(this).find('nome').text();
                    var details = $(this).find('descrizione').text();
                    var id = $(this).find('idProdotto').text();
                    var price = $(this).find('prezzo').text();
                    var available = $(this).find('disponibile').text();
                    //var limited = $(this).find('consumabile').text();
                    var quantity = $(this).find('quantita').text();

                    if(counter % 3 == 0) {
                        ++rowNum;
                        $('#menu').append('<div id="row' + rowNum + '" class="row mb-4"></div>');
                    }

                    if(available == "true") {
                        $('#row' + rowNum).append('<div class="col-4"><div class="card"><div class="cardcontainer"><h4>' + name + '</h4><p class="font-italic">' + details + '</p><ul class="list-unstyled info"><li>Prezzo: €' + price + '</li><li>Disponibili: ' + quantity + '</li></ul><button class="btn btn-light mb-2" onclick="addToCart(\'' + name + '\',' + price + ',' + id + ')" type="button"><i class="fas fa-shopping-cart fa-2x"></i></button></div></div></div>');
                    } else {
                        $('#row' + rowNum).append('<div class="col-4"><div class="card"><div class="cardcontainer"><h4>' + name + '</h4><p class="font-italic">' + details + '</p><ul class="list-unstyled info"><li>Prezzo: €' + price + '</li><li>Disponibili: ' + quantity + '</li></ul><button class="btn btn-light mb-2" onclick="addToCart(\'' + name + '\',' + price + ',' + id + ')" type="button" disabled><i class="fas fa-shopping-cart fa-2x"></i></button></div></div></div>');
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
})

//Add an item to the shopping cart
var cart = new Array();
var tot = 0;

function addToCart(nameToAdd, priceToAdd, idToAdd) {
    //Check if the item is already in the cart
    var count = 0;
    var i;

    for( i = 0; i < cart.length; ++i) {
        if(cart[i] == idToAdd) {
            ++count;
        }
    }

    //Add to cart
    cart.push(idToAdd);

    if(count == 0) {
        $('#tableBody').append('<tr id="cartRow' + idToAdd + '"><td>' + nameToAdd + '</td><td id="number' + idToAdd + '">' + 1 + '</td><td>€' + priceToAdd + '</td><td><button class="btn btn-danger" type="button" onclick="deleteRow(' + idToAdd + ',' + priceToAdd + ')">Elimina</button></td></tr>');
    } else {
        $('#number' + idToAdd).text(count+1);
    }

    //Update tot
    tot += parseFloat(priceToAdd);
    $('#tot').text(tot);

    $('#payButton').removeAttr('disabled');
}

//Remove an item from the shopping cart
function deleteRow(idToRemove, priceToRemove) {
    var count = 0;
    var i;

    for(i = 0; i < cart.length; ++i) {
        if(cart[i] == idToRemove) {
            cart.splice(i, 1);
            ++count;
            --i;
        }
    }

    $('#cartRow' + idToRemove).remove();

    //Update tot
    count *= parseFloat(priceToRemove);
    tot -= count;
    $('#tot').text(tot);

    if(tot == 0) {
        $('#payButton').attr('disabled', 'true');
    }
}

//Payment and order confirmation
function pay() {
    var formMessages = $('#bookingconfmessage');
    
    var delivery = true;
    if($('#takeaway').is(':checked')) {
        delivery = false;
    }

    var formData = "idProdotto=" + cart.toString() + "&costo=" + tot + "&consegna=" + delivery;

    $.ajax({
        type: 'POST',
        url: './api/prodotto',
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

            $('#successNotify').modal('toggle');
        },
        error: function(data) {
            alert('Oops! Errore inaspettato!');
        }
    });
}