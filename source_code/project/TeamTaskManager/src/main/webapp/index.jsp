<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Team Task Manager</title>
        <link href="./css/styles.css" rel="stylesheet" type="text/css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>

        <div class="content">
            <div class="container" style="border: none">
                <div class="page-top" style="align-items: center">
                    <h1>Správa týmových úkolů</h1>
                    <div>
                     <a class="button-like" href="login.jsp">Přihlášení</a>
                     <a class="button-like dark-blue" href="registration.jsp">Registrace</a>
                     </div>
                </div> 
                <p class="message">${message.description}</p>

                <p>Vítejte v aplikaci pro správu týmových úkolů. <a href="registration.jsp">Zaregistrujte</a> 
                    se a budete si moci vytvořit vlastní tým, do kterého poté můžete přidávat další uživatele
                    pomocí jejich ID nebo emailu.</p>
                <p>V rámcí týmů si můžete vytvořit vlastní úkoly, kterým můžete přiřadit termín, název a popis. Na vaší hlavní stránce poté uvidíte 
                úkoly vašich týmů a jestli jsou splněny. Při zobrazení jednotlivých úkolů můžete také nastavovat a kontrolovat jednotlivé podúkoly, a
                tak organizovat váš tým ještě lépe.</p>
                <p>Uživatelé v týmech mohou také obdržet jednu ze dvou rolí</p>
                <h3>Správce týmu:</h3>
                <p>Organizujte členy týmu. Přiřaďte nebo jim odeberte úkoly. Rozhodněte o tom, kdo bude členem nebo správcem týmu.</p>
                <h3>Člen týmu:</h3>
                <p>Nechte si přiřazovat úkoly nebo si vemte úkol sami na starost. Zaznamenávejte svůj postup zaškrknutím úkolů.</p>
                <p>Zadávejte úkoly, tvořte týmy a organizujte svůj čas.</p>

            </div>
        </div>

    </body>
</html>
