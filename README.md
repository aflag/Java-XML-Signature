RPS Sign
========
Fiz este projeto para assinar o XML do RPS de Belo Horizonte. Assim posso assinar um lote de notas fiscais e fazer upload no site da prefeitura.

Mais informações sobre os lotes pode ser encontrada aqui:

http://www.pbh.gov.br/bhissdigital/portal/index.php?content=nfse/documentacao.php

No caso, eu consigo assinar documentos do lote de envio contidos neste arquivo

http://www.pbh.gov.br/bhissdigital/download/nfse/Lote_RPS.zip

Como instalar o projeto
-------------------
Este projeto pode ser modificado usando o eclipse e maven. Para gerar o executável rps-sign, rode o comando (você precisa ter o maven e o make instalados):

    make

Isso irá gerar um executável rps-sign que rodará usando o java do sistema, em /usr/bin/java. O programa foi testado com openjdk 7

Se você usar debian (provavelmente Ubuntu também), você pode adicionar o seguinte repositório

    deb http://kontesti.me/debian unstable debian

Depois instale com

    apt-get install rps-sign

Uso
---
    ./rps-sign
    usage: rps-sign [-h] [-i <arg>] [-k <arg>] [-o <arg>] [-p <arg>]
     -h,--help             exibe esta mensagem
     -i,--input <arg>      arquivo de entrada (não assinado)
     -k,--key <arg>        chave PKCS12 no formato pfx
     -o,--output <arg>     arquivo de saida (assinado)
     -p,--password <arg>   senha

Licença
-------
Modifiquei o projeto em https://github.com/UrsKR/Java-XML-Signature para atender minhas necessidades. A licença original era:

    Do what you want. Whether it's modify, copy, distribute, use comercially - I don't care.
    The project comes without any warranty whatsoever.

Como eu prefiro colocar licenças conhecidas ao invés de ficar criando as minhas (usar uma já consagrada facilita para todo mundo). Eu estou distribuindo todas minhas modificações sob a licença Expat (MIT), que acho que mantem o mesmo espirito da licença original:

    Copyright (c) Rafael Cunha de Almeida <rafael@kontesti.me>

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
