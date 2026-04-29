variable "region" {
  description = "AWS region"
  default     = "ap-south-1"
}

variable "instance_type" {
  description = "EC2 instance type"
  default     = "t2.micro"
}

variable "key_name" {
  description = "Name of the AWS key pair"
  default     = "devops-key"
}

variable "ami_id" {
  description = "Ubuntu 22.04 AMI for ap-south-1"
  default     = "ami-0f58b397bc5c1f2e8"
}