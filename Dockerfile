#
# Super simple example of a Dockerfile
#
FROM ubuntu:latest
MAINTAINER Elangovan

RUN apt-get update

#install oracle jdk 8
RUN apt-get install software-properties-common python-software-properties
RUN add-apt-repository ppa:webupd8team/java
RUN apt-get install oracle-java8-installer


#GNormPlus set up
RUN wget https://www.ncbi.nlm.nih.gov/CBBresearch/Lu/Demo/tmTools/download/GNormPlus/GNormPlusJava.zip
RUN apt-get install unzip
RUN unzip GNormPlusJava.zip -d GNormPlusJava

####CRF set up for GNormPlus
RUN apt-get install build-essentials
RUN apt-get install gawk
RUN wget https://doc-00-74-docs.googleusercontent.com/docs/securesc/ha0ro937gcuc7l7deffksulhg5h7mbp1/88krvhl7omq1srbp4s9q513ibsq8m4co/1501034400000/13553212398903315502/*/0B4y35FiV1wh7d0dGRU5uRjB4dGM?e=download CRF58.zip
RUN unzip CRF58.zip -d CRF58
RUN copy -r CRF58/*  GNormPlusJava/CRF
RUN cd CRF
RUN chmod +x ./configure
RUN ./configure
RUN make
RUN make install


WORKDIR /home