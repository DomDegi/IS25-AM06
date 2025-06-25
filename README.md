<h1 align="center">
  <br>
  <a href="https://www.craniocreations.it/prodotto/galaxy-trucker">
  <img src="https://assetsio.gnwcdn.com/galaxy-trucker-re-release-cover-art.png?width=1920&height=1920&fit=bounds&quality=70&format=jpg&auto=webp" alt="Galaxy Trucker" width="900" style = "border-radius: 20px
  "></a>
  <br>
  Software Engineering Project 2025
  <br>
  Galaxy Trucker
  <br>
</h1>

<h4 align="center">A digital version of the board game <a href="https://www.craniocreations.it/prodotto/galaxy-trucker">Galaxy Trucker</a> made in Java by: <br><br>
<a href="https://github.com/SoheilAkhrraze" target="_blank" style="color: #cb3e3f">Soheil Akharraze</a><br><br>
<a href="https://github.com/fedebulfari" target="_blank" style="color: #cdaa2a	">Federico Bulfari</a><br><br>
<a href="https://github.com/EnnioCristianelli" target="_blank" style="color: #24bb4c">Ennio Cristianelli</a> <br><br>
<a href="https://github.com/DomDegi" target="_blank" style="color: #2593d5	">Domenico De Giorgio</a> <br><br>

# <img src="https://unfilteredgamer.com/wp-content/uploads/2022/02/galaxy-trucker-review-2.jpg" align="right" alt="Start Card render" width="200" style = "border-radius: 20px; float:right; padding: 5px"> <center>Our features</center>

<center>

| Features                     | Base | Advanced |
|------------------------------| ---- | -------- |
| Complete ruleset             | ✅   |          |
| Socket                       | ✅   |          |
| RMI                          | ✅   |          |
| TUI                          | ✅   |          |
| GUI                          | ✅   |          |
| Multiple Games               |      | ✅       |
| Persistence                  |      | ✅       |
| Resilience to disconnections |      | ✅       |
| Trial flight                 |      | ✅       |

</center>

# <img src="https://unfilteredgamer.com/wp-content/uploads/2022/02/galaxy-trucker-review-3.jpg" align="right" alt="Start Card render" width="200" style = "border-radius: 20px; float:right; padding: 5px"> <center>How to run the game</center>

**❗Dependencies❗** In order to run our application you need to have installed a Java version >= 23, we suggest to use [this version](https://www.oracle.com/it/java/technologies/downloads/#java23).

### 1. Run the server

You can find the server jar here: [`Server`](/deliverables/jar/GalaxyTruckerAM06_Server.jar)
Once you have downloaded the jar, you can run it with the following command:

```bash
cd path/to/GalaxyTrucker_ServerAM06.jar
java -jar GalaxyTrucker_ServerAM06.jar
```


When the server is running you will see the `ip address` of the server, you will need it to connect the client.

> **❗Warning❗** The server will run on port `1099` for RMI and `12345` Socket, make sure that these ports are available.

### 2. Run the client

You can find the client jar here: [`Client`](/deliverables/jar/GalaxyTruckerAM06_Client.jar)
Once you have downloaded the jar, you can run it with the following command:

```bash
cd path/to/GalaxyTruckerAM06_Client.jar
java -jar GalaxyTruckerAM06_Client.jar
```

By running the client you will be asked to choose the type of interface you want to use, you can choose between `TUI` and `GUI`.

> **❗Warning❗**
> The Client has been tested exclusively on machines with an `x64` architecture and `Windows 11` O.S..
>
> Note: the GUI is optimized for 100% display scaling on Windows. Other scaling settings may cause layout issues.

When the client is running you will be asked to insert the `ip address` of the server, you can find it in the server logs.

#### Alternative methods:
You can also run the server and client by executing the .exe file or by running the .bat script.

# <img src="https://unfilteredgamer.com/wp-content/uploads/2022/02/galaxy-trucker-review-6.jpg" align="right" alt="Start Card render" width="200" style = "border-radius: 20px; float:right; padding: 5px"> <center>Documentation</center>

If you are interested in the inner workings of our application you can find the following diagrams:

- [Model UMLs](deliverables/ClassDiagrams)

- [Sequence Diagrams](deliverables/SequenceDiagrams)
- [Cards' Finite State Machine](deliverables/State_final_macine_Cards.drawio.png)
- [Complete JDoc](deliverables/javadoc)

