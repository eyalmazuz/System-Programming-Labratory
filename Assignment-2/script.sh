#!/bin/bash 
clear
echo '-----------------------------------------Compiling------------------------------------------'

test_dir=""
args=""
TEST_COMPILATION=""
TEST1=""
testJunit1=""
tester_dir=""
project_dir=""


function execWIthTimout(){
	echo
	echo "Your program will receive the following arguments" 
	echo $1 $2 $3 $4 $5 $6
	echo
	cd $7
	mvn exec:java -Dexec.mainClass="bgu.spl.mics.application.BookStoreRunner" -Dexec.args="$1 $2 $3 $4 $5 $6"
}

#test runner function per json file
#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
runTestsForJsonFile(){	
	json_file_path=$1
	filename=${json_file_path##*/}
	test_num=${filename%%.*}
	test_dir_name='test_'$test_num
	test_dir=$project_dir/test_results/$test_dir_name
	args="$json_file_path $test_dir/customers.obj $test_dir/inventory.obj $test_dir/orders.obj $test_dir/moneyReg.obj $test_num $project_dir"
	rm -rf $project_dir/test_result.txt
	rm -rf $project_dir/test_errors.txt
	if [ ! -d $test_dir ]; then
		echo 'make test_dir directory'  		
		mkdir $test_dir
	fi
	echo "#########################    Running student project to create serialized output file number $test_num using $json_file_path"
	export -f execWIthTimout
	timeout 80s bash -c "execWIthTimout $args"
	# Run the tests for the current json
	echo "#########################    Running test $test_num ..."
	eval TEST$test_num="0"
	cd $project_dir
	mvn compile; mvn exec:java -Dexec.mainClass="bgu.spl.mics.application.playground" -Dexec.args="$args"
	success_string="Nice!! All tests passed!"
	#check project timed out, and then the result file was not created
	cd $project_dir
	if [ ! -f $project_dir/test_result.txt ];then
		echo 'the project with time out :( please check if you terminated well all threads'		
		echo -e $(date +"%m_%d_%Y_%T")"#########################    Your test failed! probably due to time out (but can also be caused by other issues. )\n" > $project_dir/test_result.txt
		exit 1
	fi
	firstline=$(head -n1 $project_dir/test_errors.txt)
	if [[ $firstline == *"$success_string"* ]] ; then
		eval TEST$test_num="1"
	else
		echo 'you have errors in the tests'
		echo $(date +"%m_%d_%Y_%T")"#########################    You have errors in TEST$test_num! The first line of the your log error file (can be found in test results folder) is:"
		exit 1
	fi
	echo $firstline;
	echo ""
	echo ""
	cp test_result.txt $test_dir/
	cp test_errors.txt $test_dir/
  	cd $project_dir

}

#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

print_header(){
	echo ""
	echo ""
	echo "********************************************************"
	echo "********************************************************"
	echo "********************************************************"
	echo "***                                              *******"
	echo "***                Running $1                 *******"
	echo "***                                              *******"
	echo "********************************************************"
	echo "********************************************************"
	echo "********************************************************"
}

######################################## "main" ########################################


#Run the script from inside the tester's project!

#extract all the tar.gz folders in the directory
#compile and run the student's code
tester_dir=$PWD
project_dir=$PWD
if [ ! -f $tester_dir/automatic_check.csv ];then
	echo "TEST1,UNIT_TEST1" > $tester_dir/automatic_check.csv
fi
if [ ! -d "$tester_dir/test_results/" ]; then
  mkdir test_results
fi
json_files=$tester_dir"/jsonfiles/*"
#cd to the new folder and do the following:
#Run the different tests using the different json files
#cp $json_file_path $dir/
print_header $dir

#run junit tests
#run_junit_tests
#compile the students project, if comilation fails, update flag.
mvn clean compile
rc=$?
if [[ $rc -ne 0 ]] ; then
  	echo 'Your project has comilation errors!'
fi
cd 
for json_file in $json_files;do
	echo 'jsonName: '$json_file
	runTestsForJsonFile $json_file
done
#print the students test results to the csv file
#echo -e "$TEST1" >> $tester_dir/automatic_check.csv
