import os
import re

files_to_update = [
    "est-modules/est-web-group/est-gateway/est-gateway-impl/pom.xml",
    "est-modules/est-web-group/est-gateway/est-gateway-api/pom.xml",
    "est-modules/est-microservices/est-performance/est-performance-impl/pom.xml",
    "est-modules/est-microservices/est-discovery/est-discovery-impl/pom.xml",
    "est-modules/est-microservices/est-circuitbreaker/est-circuitbreaker-impl/pom.xml",
    "est-modules/est-foundation/est-config/est-config-impl/pom.xml",
    "est-examples/pom.xml",
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

base_dir = r"d:\os\proj\java\est2.0"

for file_path in files_to_update:
    full_path = os.path.join(base_dir, file_path)
    if os.path.exists(full_path):
        print(f"Updating: {file_path}")
        with open(full_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        new_content = re.sub(r'<version>2\.0\.0</version>', '<version>2.1.0</version>', content)
        
        with open(full_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
    else:
        print(f"File not found: {file_path}")

print("Done!")
