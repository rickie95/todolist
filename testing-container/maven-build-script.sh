#! /bin/bash
# When the display is set and operative the build can finally start.


#/bin/bash
NEW_DISPLAY=33
DONE="no"
export USER=root

while [ "$DONE" == "no" ]
do
  out=$(xdpyinfo -display :${NEW_DISPLAY} 2>&1)
  if [[ "$out" == name* ]] || [[ "$out" == Invalid* ]]
  then
    # command succeeded; or failed with access error;  display exists
    (( NEW_DISPLAY+=1 ))
  else
    # display doesn't exist
    DONE="yes"
  fi
done

echo "Using first available display :${NEW_DISPLAY}"

OLD_DISPLAY=${DISPLAY}
expect /var/vnc-prompt-password.exp
vncserver ":${NEW_DISPLAY}" -localhost -geometry 1024x768 -depth 16
export DISPLAY=:${NEW_DISPLAY}

echo "Cloning repository, branch: $BRANCH"
git clone https://github.com/rickie95/todolist /var/todolist/
cd /var/todolist/
git checkout $BRANCH
mvn -f com.riccardomalavolti.apps.todolist/pom.xml $PROFILES clean verify

EXIT_CODE=$?

export DISPLAY=${OLD_DISPLAY}
vncserver -kill ":${NEW_DISPLAY}"

echo "execute-on-vnc.sh exited with $EXIT_CODE."
exit $EXIT_CODE

