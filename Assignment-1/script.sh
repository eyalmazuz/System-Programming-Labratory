#!/bin/bash

clear 
echo 'hello world: this is first time i am writing a test in bash.'
echo 'please dont blame me if something is not working well.'
echo 'please put this file in your Assignment1 folder and please add the folder files'
echo 'please ensure that files in the right location (config,input/output).'
echo 'if make/run failed then the script will be terminated.'
echo 'if you see any warnings in the build. please fix them.'
echo 'if script is not running please write in command line chmod +x ./script.sh'
echo 'if you want -> you can add more files to check: add them to the relevant varaibles and change the length value' 


make clean
make

#varaibles
#prefix:
p_files=./files/
p_config=./files/config/
p_input=./files/input/
p_output=./files/output/
#path
valgrind_log="$p_files"log.txt
config_list=("$p_config"config1.txt "$p_config"config2.txt "$p_config"myConfig_emptyLines.txt "$p_config"myConfig1.txt "$p_config"myConfig2.txt "$p_config"myConfig3.txt "$p_config"myConfig1.txt "$p_config"myConfig1.txt "$p_config"myConfig1.txt "$p_config"myConfig1.txt)
input_list=("$p_input"input_example1.txt "$p_input"input_example2.txt "$p_input"input1_justOrder.txt "$p_input"input1_justOrder.txt "$p_input"input2_justOrder.txt "$p_input"input_justOrder_empty.txt "$p_input"input3.txt "$p_input"input4.txt "$p_input"input5.txt "$p_input"input6.txt)
output_list=("$p_output"output_example1.txt "$p_output"output_example2.txt "$p_output"output1.txt "$p_output"output1.txt "$p_output"output2.txt "$p_output"output_empty.txt "$p_output"output3.txt "$p_output"output4.txt "$p_output"output5.txt "$p_output"output6.txt)
output="$p_output"output.txt
length=9

#functions
function checkResults(){
	if [ -z "$DIFF_Z" ]; then 
		echo 'pass: the output is correct'
	else
		echo 'failed: the output is not correct'
		diff -Zy "${output_list["$i"]}" "$output"
		exit 1	
	fi 

	s1='definitely lost: 0 bytes in 0 blocks'
	s2='indirectly lost: 0 bytes in 0 blocks'
	s3='possibly lost: 0 bytes in 0 blocks'
	s4='suppressed: 0 bytes in 0 blocks'
	if grep -Eq "$s1" $valgrind_log && grep -Eq "$s2" $valgrind_log && grep -Eq "$s3" $valgrind_log && grep -Eq "$s4" $valgrind_log; then
		echo 'no leak has found'
	else
		echo 'failed: there is a leak in your code(you can see in log.txt): '
		cat $valgrind_log
		exit 1
	fi 
}		

#Tests:
for i in $(seq 0 $length); do
	echo '------------------------------------------------Test'$i'------------------------------------------------' 
	echo "configuration file: " ${config_list[i]}
	echo "input file: " ${input_list[i]}		
	valgrind --leak-check=full --show-reachable=yes --log-file=$valgrind_log ./bin/rest ${config_list[i]} < ${input_list[i]} > $output
	DIFF_Z=$(diff -Z "${output_list[i]}" "$output")
  	echo "comparing between excepted file: "${output_list[i]}" and the actual file: "$output
	checkResults "$DIFF_Z" "$i" "$LEAK" "$valgrind_log" 
done

echo 'completed all tests !!!'
	
