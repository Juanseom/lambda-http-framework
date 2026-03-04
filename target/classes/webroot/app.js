document.getElementById("greetBtn").addEventListener("click", function() {
    var name = document.getElementById("nameInput").value;
    if (name === "") {
        name = "World";
    }
    fetch("/App/hello?name=" + name)
        .then(function(response) { return response.text(); })
        .then(function(data) {
            document.getElementById("result").innerText = data;
        });
});

