# elSampleroRandomo
Stream Sampler example


## How it works
Processes an input stream byte by byte and cast it to a `char`. Will generate a random number for every char and add 
it to a list. As the list reaches *k* elements, will take one random stored sample and compare the random number of it to the random number of the fresh sample from the stream. The sample with the higher random number will win and replace the one with the lower number (if the stored sample has a higher random, the list of stored samples stays the same, the new sample will be discarded).

Sampling result will have a random sequence and will not match the original sequence in the stream.
