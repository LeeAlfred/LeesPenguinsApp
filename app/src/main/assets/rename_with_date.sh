#!/bin/bash

start_date="2025-07-05"
end_date="2025-12-31"
current_date=$(date -d "$start_date" +%s)
end_timestamp=$(date -d "$end_date" +%s)
file_counter=1

for file in *.*; do
  if [[ -f "$file" ]]; then
    extension="${file##*.}"
    filename_without_ext="${file%.*}"

    if [[ "$current_date" -le "$end_timestamp" ]]; then
      formatted_date=$(date -d "@$current_date" +%Y-%m-%d)
      new_filename="${formatted_date} ${filename_without_ext}.${extension}"
      mv "$file" "$new_filename"
      current_date=$((current_date + 86400)) # Add one day in seconds
    else
      echo "Reached end date. Stopping."
      break
    fi
  fi
done

echo "Renaming process complete."
