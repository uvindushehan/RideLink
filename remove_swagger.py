import os
import re

def remove_annotation(content, annotation_name):
    pattern = r'@' + annotation_name + r'\b'
    while True:
        match = re.search(pattern, content)
        if not match:
            break
        start_idx = match.start()
        idx = match.end()
        
        while idx < len(content) and content[idx] in (' ', '\t', '\r', '\n'):
            idx += 1
            
        if idx < len(content) and content[idx] == '(':
            open_parens = 1
            idx += 1
            in_string = False
            escape = False
            while idx < len(content) and open_parens > 0:
                char = content[idx]
                if escape:
                    escape = False
                elif char == '\\':
                    escape = True
                elif char == '"':
                    in_string = not in_string
                elif not in_string:
                    if char == '(':
                        open_parens += 1
                    elif char == ')':
                        open_parens -= 1
                idx += 1
        
        # Remove trailing whitespaces up to the first newline
        while idx < len(content) and content[idx] in (' ', '\t', '\r'):
            idx += 1
        if idx < len(content) and content[idx] == '\n':
            idx += 1
            
        content = content[:start_idx] + content[idx:]
    return content

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    original = content
    content = re.sub(r'import\s+io\.swagger\.v3\.oas\.annotations.*?;[\r\n]*', '', content)
    content = re.sub(r'import\s+io\.swagger\.v3\.oas\.models.*?;[\r\n]*', '', content)
    
    annotations_to_remove = ['Operation', 'ApiResponses', 'ApiResponse', 'Tag', 'Parameter', 'Schema', 'Content', 'SecurityRequirement']
    for ann in annotations_to_remove:
        content = remove_annotation(content, ann)
        
    if original != content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Cleaned {filepath}")

for root, dirs, files in os.walk('.'):
    if 'target' in root:
        continue
    for file in files:
        if file.endswith('.java'):
            process_file(os.path.join(root, file))
