# MyNeuralNetwork

Libreria Java per costruire, eseguire e addestrare reti neurali feed-forward, scritta senza dipendenze esterne.

Progetto finale del corso di **Metodologie di Programmazione**, Sapienza Università di Roma.

## Funzionalità

- Reti con un numero arbitrario di strati, descritte da un semplice file di testo
- Funzioni di attivazione: `Step`, `Identity`, `Logistic`, `TanH`, `ReLU`
- Addestramento di reti a singolo strato con la **regola del percettrone** (funzione `Step`) e con la **delta rule** (discesa del gradiente, per le altre funzioni)
- Addestramento di reti multistrato con la **backpropagation**
- Pesi caricati da file o inizializzati casualmente
- Serializzazione della rete, pesi inclusi, nello stesso formato di input

## Struttura

```text
src/mynn/
├── IReteNeurale.java           interfaccia pubblica e caricamento da file
├── ReteNeurale.java            rete come sequenza di strati
├── StratoDiNeuroni.java        strato di neuroni con funzione di attivazione comune
├── Neurone.java                somma pesata, attivazione e aggiornamento dei pesi
└── FunzioniDiAttivazione.java  funzioni di attivazione e derivate
test/mynn/                      test JUnit 4
lib/                            JUnit e Hamcrest, necessari per i test
reti/                           reti di esempio (AND, OR, XOR, NOT, classificazione, x²)
```

## Formato del file di rete

La prima riga contiene il nome della rete, ogni riga successiva descrive uno strato:

```text
nome=ReteXor
layer={ nome=input activationFunction=Logistic inputUnits=2 outputUnits=2 weights=[[1,1,0],[2,2,0]] }
layer={ nome=output activationFunction=Logistic inputUnits=2 outputUnits=1 weights=[[-1000,850,0]] }
```

| Campo | Descrizione |
| --- | --- |
| `nome` | nome dello strato |
| `activationFunction` | funzione di attivazione dei neuroni dello strato |
| `inputUnits` | numero di input di ciascun neurone |
| `outputUnits` | numero di neuroni dello strato |
| `weights` | *(opzionale)* un gruppo per neurone con `inputUnits + 1` valori: i pesi degli input seguiti dal bias. Se assente, i pesi sono inizializzati casualmente in [0, 1) |

Il numero di neuroni di ogni strato deve coincidere con `inputUnits` dello strato successivo.

## Esempio d'uso

```java
IReteNeurale rete = IReteNeurale.carica("reti/retePercettrone");

double[][] inputs  = { {0.0}, {1.0} };
double[][] outputs = { {1.0}, {0.0} };      // funzione NOT

rete.train(inputs, outputs);

System.out.println(rete.process(new double[]{0.0})[0]);   // 1.0
System.out.println(rete);                                 // rete con i pesi appresi
```

## Compilazione e test

Requisiti: Java 8 o superiore. Le librerie per i test (JUnit 4 e Hamcrest) sono incluse in `lib/`.

I comandi vanno lanciati dalla cartella principale del progetto: compilano sorgenti e test in `bin/` ed eseguono i test con JUnit.

Windows (PowerShell):

```powershell
javac -encoding UTF-8 -cp "lib\*" -d bin src\mynn\*.java test\mynn\*.java
java -cp "bin;lib\*" org.junit.runner.JUnitCore mynn.TestReteNeuraleStampa
```

Linux / macOS:

```sh
javac -encoding UTF-8 -cp "lib/*" -d bin src/mynn/*.java test/mynn/*.java
java -cp "bin:lib/*" org.junit.runner.JUnitCore mynn.TestReteNeuraleStampa
```

Al posto di `mynn.TestReteNeuraleStampa` si possono indicare una o più classi di test:

| Classe di test | Verifica |
| --- | --- |
| `TestReteNeuraleNoTrain` | caricamento da file, output delle porte logiche, serializzazione |
| `TestReteNeuralePerceptron` | addestramento del percettrone |
| `TestReteNeuraleQualsiasiFunzione` | addestramento con la delta rule |
| `TestReteNeurale` | addestramento di una rete a due strati con la backpropagation |
| `TestReteNeuraleStampa` | stampa a console l'output delle reti e l'andamento dell'errore durante l'addestramento |

Il progetto include la configurazione per Eclipse e VS Code (`.project`, `.classpath`, `.settings/`).
