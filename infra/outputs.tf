output "public_ip" {
  description = "Public IP of EC2 instance"
  value       = aws_instance.auth_server.public_ip
}

output "ssh_command" {
  description = "SSH command to connect"
  value       = "ssh -i devops-key.pem ubuntu@${aws_instance.auth_server.public_ip}"
}