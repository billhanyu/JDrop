# JDrop
JDrop simple Java application to transmit short text and small files using Socket APIs. It uses Java's native implementation of ServerSocket to provide a server running in background, while at the same time uses Socket class to provide an endpoint from which a text or a file can be sent.

## Usage
You must have JVM installed to run this program. You can either run from command line or double click on the icon to run it. Once it's up and running, it will be able to process incoming transmissions from another computer running the same application.

To send a text, copy and paste the text or manually type in a string of text in the Text field. To send a file, click Browse... and choose the desired file. Note that you can only send text or file at a time.

A 4-digit random authentication code is used to prevent unauthorized attempts to transmit files. Incoming transmission with wrong authentication codes will be discarded. The code will be re-generated automatically once a successful transmission has been made.

## Compiling
All required libraries have been added to sources, so you shouldn't run into problems for that. The two required libraries are Apache Commons IO and Commons Lang.

## License
Copyright 2016 Morton Mo

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
