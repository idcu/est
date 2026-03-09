import os

base_dir = r"d:\os\proj\java\est2.0"

files = [
    "est-examples/est-examples-web/pom.xml",
    "est-examples/est-examples-microservices/pom.xml",
    "est-examples/est-examples-microservices/est-examples-microservices-user/pom.xml",
    "est-examples/est-examples-microservices/est-examples-microservices-order/pom.xml",
    "est-examples/est-examples-microservices/est-examples-microservices-gateway/pom.xml",
    "est-examples/est-examples-graalvm/pom.xml",
    "est-examples/est-examples-features/pom.xml",
    "est-examples/est-examples-basic/pom.xml",
    "est-examples/est-examples-ai/pom.xml",
    "est-examples/est-examples-advanced/pom.xml",
    "est-demo/pom.xml"
]

for filepath in files:
    full_path = os.path.join(base_dir, filepath)
    if os.path.exists(full_path):
        with open(full_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        if '<version>2.0.0</version>' in content:
            new_content = content.replace('<version>2.0.0</version>', '<version>2.1.0</version>')
            with open(full_path, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Updated: {filepath}")
        else:
            print(f"No 2.0.0: {filepath}")
    else:
        print(f"Missing: {filepath}")

print("Done!")
