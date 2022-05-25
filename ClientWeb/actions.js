$(function (){
    const URL_SHOW = 'http://localhost:8080/festival/shows';
    refreshTable()

    function refreshTable(){
        $.ajax({
            type: 'GET',
            url: URL_SHOW,
            contentType: 'application/json',
            data: "",
            success: function (response, status, request){
                $("#shows tr").each(function (){
                    let indexRow = $(this).index();
                    if(indexRow > 0)
                        $(this).remove();
                })

                let array = eval( '(' + request.responseText + ')');
                console.log(array);

                for(let i=0;i<array.length;i++){
                    let artist_name = array[i].artistName;
                    let dataAndTime = array[i].dataAndTime;
                    let tickets_available = array[i].ticketsAvailable;
                    let tickets_sold = array[i].ticketsSold;
                    let id = array[i].id;
                    let id_locatie = array[i].location;
                    console.log(id);

                    let tablerow = '<tr>' +
                        '<td>' + id + '</td>' +
                        '<td>' + artist_name + '</td>' +
                        '<td>' + dataAndTime + '</td>' +
                        '<td>' + tickets_available + '</td>' +
                        '<td>' + tickets_sold + '</td>' +
                        '<td>' + id_locatie.id + '</td>' +
                        '</tr>';
                    $("#shows").append(tablerow);
                }

                $("#shows tr").click(function (){
                    let indexRow = $(this).index();
                    if(indexRow === 0)
                        return;
                    let id = $(this).find('td').eq(0).text();
                    let artist_name = $(this).find('td').eq(1).text();
                    let dataAndTime = $(this).find('td').eq(2).text();
                    let tickets_available = $(this).find('td').eq(3).text();
                    let tickets_sold = $(this).find('td').eq(4).text();
                    let id_locatie = $(this).find('td').eq(5).text();

                    $("#id_show").val(id);
                    $("#artist_name").val(artist_name);
                    $("#dataAndTime").val(dataAndTime);
                    $("#location").val(location);
                    $("#tickets_available").val(tickets_available);
                    $("#tickets_sold").val(tickets_sold);
                    $("#id_locatie").val(id_locatie);
                })
            }
        })
    }

    $("#save").click(function (){
        let artist_name = $("#artist_name").val();
        let dataAndTime = $("#dataAndTime").val();
        let tickets_available = $("#tickets_available").val();
        let tickets_sold = $("#tickets_sold").val();
        let id_locatie = $("#id_locatie").val();

        console.log(dataAndTime);

        $.ajax({
            type: 'POST',
            url: URL_SHOW,
            contentType: "application/json",
            data: JSON.stringify({
                "artistName": artist_name,
                "dataAndTime": dataAndTime,
                "location": {
                    "id": id_locatie
                },
                "ticketsAvailable": tickets_available,
                "ticketsSold": tickets_sold
            }),
            success: function (response, status, request){
                alert('The show was saved successfully: ' + request.responseText);
                refreshTable();
            },
            error: function (response){
                alert("Error save show: " + response.responseText);
            }
        })
    });

    $("#update").click(function (){
        let id = $("#id_show").val();
        let artist_name = $("#artist_name").val();
        let dataAndTime = $("#dataAndTime").val();
        let tickets_available = $("#tickets_available").val();
        let tickets_sold = $("#tickets_sold").val();
        let id_locatie = $("#id_locatie").val();

        $.ajax({
            type: 'PUT',
            url: URL_SHOW + '/' + id,
            contentType: 'application/json',
            data: JSON.stringify({
                "artistName": artist_name,
                "dataAndTime": dataAndTime,
                "location": {
                    "id": id_locatie
                },
                "ticketsAvailable": tickets_available,
                "ticketsSold": tickets_sold,
                "id": id
            }),
            success: function (response, status, request){
                alert('The show was updated successfully: ' + request.responseText);
                refreshTable();
            },
            error: function (response){
                alert("Error update show: " + response.responseText);
            }
        })
    })

    $("#delete").click(function (){
        let id = $("#id_show").val();
        $.ajax({
            type: 'DELETE',
            url: URL_SHOW + '/' + id,
            contentType: 'application/json',
            data: "",
            success: function (response, status, request){
                alert("The show was deleted successfully");
                refreshTable();
            },
            error: function (response){
                alert("Error delete show: " + response.responseText);
            }
        })
    })

})