for f in *.csv; do
	python3 count-rows.py "$f"
done
