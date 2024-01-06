for file in $(ls "composables"); do
  # newfile=$(echo "$file "| sed "s/\(.*\)Activity.kt/\1Screen.kt/g")
  echo "$file"
  cp "composables/AddExpenseScreen.kt" "composables/$file"
done
