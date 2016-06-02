<#-- @ftlvariable name="" type="org.yukung.sandbox.dropwizard.sample.views.PersonView" -->
<html>
<body>
<!-- calls getPerson().getFullName() and sanitizes it -->
<h1>Hello, ${person.fullName?html}!</h1>

<p>文字コード：${charset}</p>
</body>
</html>
