#!/bin/bash
#------------------------------------------------------------------------------
# model: cfg_get
# args: [1] => IN:<configure-file>
#       [2] => IN:<key>
#       [3] => OUT:<value-as-env-var>
# describe: get configure value by key from a configure file
# example:
#   > cfg_get "./Anubis.ini" "Anubis.ORB1.NSLocation" "OUT_MYVAR"
#   > echo $OUT_MYVAR
#   file:///etc/iors/ACNS_GlobalDev.ior
# Usage: getcfg <in:file> <in:key> <out:valueENV>
#------------------------------------------------------------------------------

cfg_get() {
	[[ -f "$1" && ! -z "$2" && ! -z "$3" ]] || return 1
	export $3="$(cat "$1" | sed -n "/^$2/{ s~^[^=]*= \(.*\)$~\1~g; p; }" | tail -n 1)"
	#export $3=“$(echo $(cat "$1" | sed -n "/^$2/{ s~^[^=]*= \(.*\)$~\1~g; p; }" | tail -n 1)|cut -f2 -d=)” 

}

#------------------------------------------------------------------------------
# model: cfg_set
# args: [1] => IN:<configure-file>
#       [2] => IN:<key>
#       [3] => IN:<value>
# describe: set key = value to configure file
# example:
#   > cfg_set "./Anubis.ini" "Anubis.ORB1.NSLocation" "file:///etc/iors/ACNS_GlobalDev.ior"
#   > cat "./Anubis.ini"
#   Anubis.ORB1.NSLocation = file:///etc/iors/ACNS_GlobalDev.ior
#------------------------------------------------------------------------------

cfg_set()
{
  test -f "$1" && test ! -z "$2" && test ! -z "$3"
  if [ $? -eq 0 ]; then
    sed '/^'"$2"' =/{ s~^.*$~'"$2"' = '"$3"'~g }' -i "$1"
  fi
}

test -f "$1" && {

 
  echo "contents of config.ini"
  echo "----------------------------------------------"
  cat "$1"
  echo "----------------------------------------------"
  echo

  cfg_get "$1" "D" "CFG_D"
  cfg_get "$1" "PLAYERS1" "CFG_PLAYERS1"
  cfg_get "$1" "PLAYERS2" "CFG_PLAYERS2"
  cfg_get "$1" "OUTPUT" "CFG_OUTPUT"
  cfg_get "$1" "ERROUTPUT" "CFG_ERROUTPUT"
  VAR_D=$(echo $CFG_D|cut -f2 -d=)
  VAR_PLAYERS1=$(echo $CFG_PLAYERS1|cut -f2 -d=)
  VAR_PLAYERS2=$(echo $CFG_PLAYERS2|cut -f2 -d=)
  VAR_OUTPUT=$(echo $CFG_OUTPUT |cut -f2 -d=)_$(date "+%Y%m%d%H%M%S")
  VAR_ERROUTPUT=$(echo $CFG_ERROUTPUT |cut -f2 -d=)

echo "------------The test starts-------------"
for player1 in ${VAR_PLAYERS1[@]}
do
    echo $player1
    for player2 in ${VAR_PLAYERS2[@]}
    do
       echo $player2
       for d in ${VAR_D[@]}
       do 
	  echo $d
            java offset.sim.Offset $d $player1 $player2 output >> $VAR_OUTPUT
       done
    done
done
echo "-----------------Done!----------------"
  


}

