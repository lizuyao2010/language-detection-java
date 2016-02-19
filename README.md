# language-detection
language deteciton program written in java
# classifier
naive bayes classifier.
# features
features are character level ngrams in the text.
# model
trained model using bag of n-grams
# usage
##train
java -cp out/production/nlp:json-simple-1.1.jar LanguageDetector train nlp/model nlp/TrainData
##test
java -cp out/production/nlp:json-simple-1.1.jar LanguageDetector test nlp/model nlp/TestData 2
