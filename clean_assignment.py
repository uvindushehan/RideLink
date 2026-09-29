import os
import re

def clean_file(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()
        
    new_lines = []
    changed = False
    
    for line in lines:
        if 'IT3130' in line or 'AD Group Assignment' in line:
            changed = True
            
            # Handle POM description
            if '<description>' in line:
                line = re.sub(r' - IT3130 AD Group Assignment', '', line)
                new_lines.append(line)
            # Handle JavaDoc lines
            elif '* <p>IT3130 AD Group Assignment' in line or '* IT3130 AD Group Assignment' in line:
                # Just skip this line entirely
                continue
            # Handle application.properties or README
            elif line.strip().startswith('# IT3130') or line.strip().startswith('**Assignment:**'):
                continue
            else:
                # Fallback: just strip the string
                line = re.sub(r'IT3130.*?Assignment(?: — RideLink System)?', '', line)
                new_lines.append(line)
        else:
            new_lines.append(line)
            
    if changed:
        with open(filepath, 'w') as f:
            f.writelines(new_lines)
        print(f"Cleaned {filepath}")

for root, dirs, files in os.walk('.'):
    if 'target' in root or '.git' in root:
        continue
    for file in files:
        if file.endswith(('.java', '.xml', '.properties', '.md')):
            filepath = os.path.join(root, file)
            clean_file(filepath)
