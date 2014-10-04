Run the bash script:
Input in the terminal: ./test_offset_config.sh offset_config.ini
------------------------------------------------------------------------------------
offset_config.ini is the configuration file. We can change the parameters such as d, players1, players2, output here.
------------------------------------------------------------------------------------
If we want to test our player with other players, we should change the main() function in the sim folder: comment game.playgui(), uncomment game.play(). Or, we can pass another parameter “True of False ”to the main() fucntion to choose whether playing with gui or not.
——----------------------------------------------------------------------------------
Now the output file contains information of standard output of the progam. If we want a more structured output for further analysis, we should change the System.out.println in the code.
------------------------------------------------------------------------------------
As the (p,q) pair is randomly generated in Bingyi’s code, if we want to test all the (p,q) pairs under a certain d, we should make a little change of the code.

