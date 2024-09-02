<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
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
</body>
</html>