FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    lsb-release \
    software-properties-common \
    openjdk-21-jdk \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs

WORKDIR /workspace

EXPOSE 3000

EXPOSE 8080

CMD ["bash"]