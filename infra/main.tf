terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.region
}

# ---------------------------
# Security Group
# ---------------------------
resource "aws_security_group" "auth_sg" {
  name        = "auth-service-sg"
  description = "Allow SSH, Jenkins, App"

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # tighten later
  }

  ingress {
    description = "Jenkins UI"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Jenkins Agent"
    from_port   = 50000
    to_port     = 50000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ---------------------------
# EC2 Instance
# ---------------------------
resource "aws_instance" "auth_server" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  key_name               = var.key_name
  vpc_security_group_ids = [aws_security_group.auth_sg.id]

  user_data = <<-EOF
              #!/bin/bash
              set -euxo pipefail

              # Log everything
              exec > /var/log/user-data.log 2>&1

              echo "=== START ==="

              apt-get update -y

              # Install base packages
              apt-get install -y \
                docker.io \
                curl \
                ca-certificates \
                fontconfig \
                openjdk-17-jre

              # Start Docker
              systemctl enable docker
              systemctl start docker
              usermod -aG docker ubuntu

              echo "=== DOCKER INSTALLED ==="

              # Install Docker Compose (manual, stable)
              curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" \
                -o /usr/local/bin/docker-compose

              chmod +x /usr/local/bin/docker-compose
              docker-compose --version

              echo "=== DOCKER COMPOSE INSTALLED ==="

              docker run -d \
              -p 8080:8080 \
              -p 50000:50000 \
              --name jenkins \
              --restart unless-stopped \
              -v jenkins_home:/var/jenkins_home \
              jenkins/jenkins:lts

              # Status check (non-blocking)
              systemctl status docker || true

              echo "=== SETUP COMPLETE ==="
              EOF

  tags = {
    Name = "auth-service-server"
  }
}