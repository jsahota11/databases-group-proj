import sys
import pandas as pd

if len(sys.argv) < 2:
    print("Usage: python script.py <filename>")
    sys.exit(1)

filename = sys.argv[1]

try:
    if filename.endswith(".csv") or filename.endswith(".txt"):
        df = pd.read_csv(filename, encoding="latin1")  # or encoding="ISO-8859-1"
    elif filename.endswith(".xlsx"):
        df = pd.read_excel(filename)
    elif filename.endswith(".json"):
        df = pd.read_json(filename)
    else:
        print("Unsupported file type.")
        sys.exit(1)

    print(f"Number of rows: {len(df)}")

except UnicodeDecodeError:
    print("Encoding issue: try using encoding='latin1' or encoding='ISO-8859-1'")

