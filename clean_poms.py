import os
import re

def process_pom(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Regex to match the springdoc dependency block
    pattern = r'<dependency>\s*<groupId>org\.springdoc</groupId>\s*<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>.*?<\/dependency>'
    
    new_content = re.sub(pattern, '', content, flags=re.DOTALL)
    
    if new_content != content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Cleaned {filepath}")

def process_properties(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()
        
    new_lines = []
    changed = False
    for line in lines:
        if 'springdoc' in line or 'Swagger' in line:
            changed = True
            continue
        new_lines.append(line)
        
    if changed:
        with open(filepath, 'w') as f:
            f.writelines(new_lines)
        print(f"Cleaned {filepath}")

for root, dirs, files in os.walk('.'):
    if 'target' in root:
        continue
    for file in files:
        if file == 'pom.xml':
            process_pom(os.path.join(root, file))
        elif file == 'application.properties':
            process_properties(os.path.join(root, file))
