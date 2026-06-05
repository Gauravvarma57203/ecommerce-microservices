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

# -----------------------------------
# Read Jenkins compose file
# -----------------------------------

locals {
  jenkins_compose = file("${path.module}/jenkins/docker-compose.yml")
}

# -----------------------------------
# Security Group
# -----------------------------------

resource "aws_security_group" "devops_sg" {
  name        = "devops-sg"
  description = "Allow Jenkins and app traffic"

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Jenkins"
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

  ingress {
    description = "Application"
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Postgres"
    from_port   = 5432
    to_port     = 5432
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

# -----------------------------------
# EC2 Instance
# -----------------------------------

resource "aws_instance" "devops_server" {

  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = var.key_name

  vpc_security_group_ids = [
    aws_security_group.devops_sg.id
  ]

  user_data = <<-EOF
              #!/bin/bash
              set -euxo pipefail

              exec > /var/log/user-data.log 2>&1

              echo "===== STARTING SETUP ====="

              apt-get update -y

              apt-get install -y \
                docker.io \
                curl \
                openjdk-17-jre

              systemctl enable docker
              systemctl start docker

              usermod -aG docker ubuntu

              curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" \
                -o /usr/local/bin/docker-compose

              chmod +x /usr/local/bin/docker-compose

              docker-compose --version

              echo "===== DOCKER READY ====="

              mkdir -p /home/ubuntu/jenkins

              cat <<'EOT' > /home/ubuntu/jenkins/docker-compose.yml
              ${local.jenkins_compose}
              EOT

              cd /home/ubuntu/jenkins

              docker-compose up -d

              echo "===== JENKINS STARTED ====="

              docker ps

              echo "===== SETUP COMPLETE ====="
              EOF

  tags = {
    Name = "devops-server"
  }
}