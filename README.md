# trafficklights

Application developed for the final paper in computer science.

Re-natal é a ferramenta utilizada para criar os aplicativos em clojurescript com reagent em React Native (https://github.com/drapanjanas/re-natal).

Para instalar o re-natal é preciso instalar suas dependências:

`pm install -g react-native-cli`

## Watchman

```
git clone https://github.com/facebook/watchman.git
cd watchman
git checkout v4.9.0  # the latest stable release
./autogen.sh
./configure
make
sudo make install
```

## Node.js 
Uma das dependencias para o re-natal é o node.js. Para instalar basta instalar o gerenciador de versões do node com:
https://github.com/creationix/nvm

```
wget -qO- https://raw.githubusercontent.com/creationix/nvm/v0.33.8/install.sh | bash
nvm install 8.10.0
vm use 8.10.0
```


## Leiningen
Instalando o java:
`sudo apt-get install openjdk-8-jre-headless`

Tutorial:https://lispcast.com/clojure-ubuntu/

Download do script em:https://leiningen.org/

Link: https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein

```
sudo mkdir -p /usr/local/bin/
sudo mv ~/Downloads/lein* /usr/local/bin/lein
sudo chmod a+x /usr/local/bin/lein
export PATH=$PATH:/usr/local/bin
```

## Criando um projeto do re-natal
`sudo npm install -g re-natal`

Para criar o projeto:

`re-natal init NomeDoProjeto` 

Com o projeto criado, para rodar no android é preciso instalar o ADB: 

`sudo apt install android-tools-adb`

Para iniciar o react native na pasta do projeto:

```
react-native start --reset-cache 
re-natal use-android-device real
re-natal use-figwheel
lein figwheel android
react-native run-android
```

Para mostrar os logs de execução:

`react-native log-android`

## Erros

Pode ser necessário ter que criar uma variavel para o caminho do android sdk, para criar no shell local: `export ANDROID_HOME="/home/user/Android/Sdk"`

Erro ao iniciar o react-native em mergedebugresources durante a build, pode ser resolvido com:
`sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386`

Ao iniciar o react-native deu erro no JDK:
`sudo apt-get install openjdk-8-jdk`

Para se comunicar com o android, o react-native utiliza o adb reverse apartir do android 5.0 (http://facebook.github.io/react-native/docs/running-on-device.html).
Em versões anteriores é preciso ativar o debug, via wifi. Após o comando react-native run android irá aparecer um erro na tela, após isso deverá ser aberto o menu de desenvolvimento:

`adb shell input keyevent 82`

Após isso deverá ser selecionada a opção:
"Dev Settings" > "Debug server host & port for device"

Devera ser informado o IP e a porta, ex: 192.168.1.2:8081

Para utilizar o figwheel, também será necessário alterar o IP no arquivo index.android.js na raiz do projeto, ficará assim:

`start('NomedoProjeto','android','192.168.1.2');`

## Usage

FIXME

## License

Copyright © 2018 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
