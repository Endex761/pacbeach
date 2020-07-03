//Check if the user is logged
$(document).ready(function() {
    $.ajax({
        type: 'GET',
        url: './api/login',
        dataType: 'text xml',
        success: function(xml) {
            xml = $(xml);
            var succ = xml.find('success').text();
            var message = xml.find('message').text();

            if(succ == "true") {
                $('#usrIcon').attr('data-target', '#');

                switch(message) {
                    case "utente":
                        $('#usrButtom').attr('href', './user');
                            
                        $('#ticketButton').attr('href', './ticket');
                        $('#ticketButton').removeAttr('data-toggle');
                        $('#ticketButton').removeAttr('data-target');
    
                        $('#barButton').attr('href', './bar');
                        $('#barButton').removeAttr('data-toggle');
                        $('#barButton').removeAttr('data-target');
                        break;
                    case "barista":
                        window.location.replace('./barista');
                        break;
                    case "bagnino":
                        window.location.replace('./lifeguard');
                        break;
                    case "cassiere":
                        window.location.replace('./cashier');
                        break;
                }
            }
        }
    })
})

//Check password lenght and reset the form
function controllo() {
    var p = document.getElementById('password').value;

    if(p.length >= 8) {
        document.getElementById('f').reset();
        //document.getElementById('confn').style.display='block';
        document.getElementById('conf').disabled = true;
    }
    else {
        //document.getElementById('err').style.display='block';
        alert("La password deve contenere almeno 8 caratteri!");
    }
}

//Live check on password quality
function bonta() {
    var p = document.getElementById('password').value;
    var cp = document.getElementById('cpassword').value;
    var counter = 0; //Password quality [1-5]
    var corta = 1;
    
    //lenght
    if(p.length >= 8) {
        counter+=25;
        corta = 0;
    }

    //lower case
    var caratteriMin = /[a-z]/;
    if(p.match(caratteriMin)) {
        counter+=25;
    }

    //upper case
    var caratteriMaiusc = /[A-Z]/;
    if(p.match(caratteriMaiusc)) {
        counter+=25;
    }
    
    //numbers
    var numeri = /[0-9]/;
    if(p.match(numeri)) {
        counter+=25;
    }
    
    if(corta == 1) {
        document.getElementById("bon").style.color = "#B81818";
        document.getElementById("bon").innerHTML = "Password molto debole! Inserisci almeno 8 caratteri!";
    }
    else{
        switch(counter) {
            case 25:
                document.getElementById("bon").style.color = "#B81818";
                document.getElementById("bon").innerHTML = "Password molto debole!";
                break
            case 50:
                document.getElementById("bon").style.color = "#B81818";
                document.getElementById("bon").innerHTML = "Password debole!";
                break
            case 75:
                document.getElementById("bon").style.color = "#C2B913";
                document.getElementById("bon").innerHTML = "Password buona!";
                break
            case 100:
                document.getElementById("bon").style.color = "#219E21";
                document.getElementById("bon").innerHTML = "Password ottima!";
                break
        }
    }
}

//Live check on password and confirm matching
function live() {
    var cp = document.getElementById("cpassword").value;
    var p = document.getElementById("password").value;

    var l = new RegExp('^' + cp);

    if(p.match(l) && p.length == cp.length) {
        document.getElementById("conf").disabled = false;
        document.getElementById("error").style.color = "#219E21";
        document.getElementById("error").innerHTML = "Le password coincidono!";
    }
    else{
        document.getElementById("error").style.color = "#B81818";
        document.getElementById("error").innerHTML = "Attenzione! Le password non coincidono!";
        document.getElementById("conf").disabled = true;
    }
}

//Submit sign up form
$(function() {
    var form = $('#suf');
    var formMessages = $('#suconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        var formData = $(form).serialize();

        $.ajax({
            type: 'POST',
            url: $(form).attr('action'),
            data: formData,
            dataType: 'text xml',
            success: function(xml) {
                xml = $(xml);
                var succ = xml.find('success').text();
                var message = xml.find('message').text();

                $(formMessages).attr('style', 'color: green');

                $(formMessages).text(message);
            },
            error: function(data) {
                $(formMessages).attr('style', 'color: red');
                $(formMessages).text('Oops! Errore inaspettato!');
            }
        });
    });
})

//Submit sign in form
$(function() {
    var form = $('#sif');
    var formMessages = $('#siconfmessage');

    $(form).submit(function(event) {
        event.preventDefault();

        var formData = $(form).serialize();

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
                    $('#usrIcon').attr('data-target', '#');

                    switch(message) {
                        case "utente":
                            $('#usrButtom').attr('href', './user');
    
                            $('#signinup').modal('toggle');
                            $('#successNotify').modal('toggle');
                            
                            $('#ticketButton').attr('href', './ticket');
                            $('#ticketButton').removeAttr('data-toggle');
                            $('#ticketButton').removeAttr('data-target');
    
                            $('#barButton').attr('href', './bar');
                            $('#barButton').removeAttr('data-toggle');
                            $('#barButton').removeAttr('data-target');
                            break;
                        case "barista":
                            window.location.replace('./barista');
                            break;
                        case "bagnino":
                            window.location.replace('./lifeguard');
                            break;
                        case "cassiere":
                            window.location.replace('./cashier');
                            break;
                    }
                } else {
                    $(formMessages).attr('style', 'color: red');
                    $(formMessages).text(message);
                }
            },
            error: function(data) {
                $(formMessages).attr('style', 'color: red');
                $(formMessages).text('Oops! Errore inaspettato!');
            }
        });
    });
})