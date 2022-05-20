# Task1-Function Plotter

This repository contains the documentation for [Task1-Function Plotter] .

#### Contents

- [Overview](#1-overview)
- [Functions](#2-Functions)
  - [calculateScalePoints](#2.1.-calculateScalePoints)
  - [addAllEquationCharacters](#2.2.-addAllEquationCharacters)
  - [validateEquation](#2.3-validateEquation)
  - [translateEquation](#2.4-translateEquation)
  - [calculatePoints](#2.5-calculatePoints)
  - [changeElementsFromEquation](#2.6-changeElementsFromEquation)
  - [getAllPoints](#2.7-getAllPoints)
  - [createDataset](#2.8-createDataset)
  - [XYLineChart_AWT](#2.9-XYLineChart_AWT)
- [Technologies](#3-technologies)

## 1. Overview

#### TopologyAPI is a GUI Desktop Application.
It Takes an Equation and minimum value and maximum value from the User and check if it the equation right or not then parse it and Plot a graph with X and Y coordinates of the equation and draw the line of it based on the points that the application will get from the max and min number.


## 2. Functions

### 2.1. calculateScalePoints

#### Dividing the minimum and maximum value into a number of X points based on a specific scale .


### 2.2. addAllEquationCharacters

#### adds all the equation characters that can be in the equation in order to use it in validating the equation 


### 2.3. validateEquation

#### Checks if the Given Equation is written in a right syntax that can be evaluated afterwards or it is not a valid equation.
returns true if it is a valid equation or false otherwise.


### 2.4. translateEquation

#### Evaluates the equation string to a readable equation variables, numbers, and characters in order to get the Y Points from it.


### 2.5. calculatePoints

#### Gets one X and Y Points by substituting the all the variable x with the number passed to it and applying Operator precedence.
returns X and Y Points.
 

### 2.6. changeElementsFromEquation

#### Substitutes some elements of the function which have high percedence with it's answer.

### 2.7. getAllPoints

#### Gets all the X and Y Points by based on the X points The system got from function CalculateScalePoints and Passeng them to function translateEquation and returning the.
returns array of x and y points.

### 2.8. createDataset

#### Creates A data set which contains all the X and Y Points we got from function getAllPoints returning the data set to use it in drawing the graph.

### 2.9. XYLineChart_AWT

#### Drawing the Graph of the Equation based on the data set that was passed from function createDataset and representing the Graph in the GUI.

# 3. Technologies

I Used To Make The GUI 

#### 1- jfreechart Library
#### 2- jcommon Library
#### 3- swing Librari 





