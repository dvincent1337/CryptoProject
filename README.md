CryptoProject
=============
Authors: David Vincent, Ben Greer

Dependencies:

commons-io-2.4				- for file IO <br>
jchart2D-3.2.2				- for chart visualizations <br>
jcommon-1.0.20				- for jchart2D <br>
jide-oss.2.9.7				- for jchart2D <br>
neurpoh-core-2.8			- for Neural Networks <br>
xmlgraphics-commons-1.3.1	- for jchart2D <br>

=============
The goal of this project was to develop a gui tool for encrypting and decrypting the Vigenère cipher, which used neural netowrks to help with decrypting. Two variants of the  Vigenère cipher were used: mono alphabetic, and poly alphabetic. The reason neural networks were used, is because using the cpyhers, they can be brute forced, however it is diffucult to know when success has occured. So we decided to use freqneny analysis to determine if the input text was: english, mono alphabetic, or poly alphabbetic text. With this ability, the program was then able to take a block of input text and decypt it to english by deciding what encryption it was using (none,mono,poly) and begin decrypting it until it was finished (english).

The program was successful, and aftwer we trained it useing thouands of lines of text encrypted and decrypted, it was able to work about 95% of the time. Additional features were added to the program such as the ability to load and store training data, and to be able to save it's current state, as well as the ability to plot all of the traning data in each category, to get a visual representation of the frequency chart. 
