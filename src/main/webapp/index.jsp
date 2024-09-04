<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <script>
        function updateExchangeRate() {
            const exchangeRateCode = document.getElementById("exchangeRateCode").value;
            const rate = document.getElementById("rate").value;
            console.log("Rat:")
            console.log("Rate" + rate)
            console.log("Rate end")
            fetch(`http://localhost:8080/exchange_war_exploded/exchangeRates/USDEUR`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    rate: rate
                })
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to update exchange rate');
                    }
                })
                .then(data => {
                    alert("Currency pair updated successfully: " + JSON.stringify(data));
                })
                .catch(error => {
                    alert("Error: " + error.message);
                });
        }
    </script>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="currencies">All Currencies</a>
<br/>
<br/>
<a href="exchangeRates">All Exchange Rates</a>
<br/>
<br/>
<form method="post" action="currencies" enctype="application/x-www-form-urlencoded">
    <label>Add new currency</label>
    <input type="text" name="name" placeholder="Name">
    <input type="text" name="code" placeholder="Code">
    <input type="text" name="sign" placeholder="Sign">
    <button type="submit" value="Submit">Send</button>
</form>
<br/>
<br/>
<form method="post" action="exchangeRates" enctype="application/x-www-form-urlencoded">
    <label>Add new exchange rate</label>
    <input type="text" name="baseCurrencyCode" placeholder="Base Currency Code">
    <input type="text" name="targetCurrencyCode" placeholder="Target Currency Code">
    <input type=text name="rate" placeholder="rate">
    <button type="submit" value="Submit">Send</button>
</form>
<br>
<br>
<form onsubmit="event.preventDefault(); updateExchangeRate();">
    <label for="exchangeRateCode">Change rate:</label>
    <input type="text" id="exchangeRateCode" name="exchangeRateCode" placeholder="Currency code" required>
    <br>
    <label for="newRate">New Rate:</label>
    <input type="text" id="rate" name="rate" placeholder="rate" required>
    <br>
    <button type="submit">Update</button>
</form>
</body>
</html>