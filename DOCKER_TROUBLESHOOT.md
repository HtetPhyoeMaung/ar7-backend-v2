# Docker Image Pull Troubleshooting Guide

## Issue: TLS: bad record MAC error when pulling Docker images

This error indicates a network/TLS connectivity issue with Docker Hub.

## Solutions (try in order):

### 1. Restart Docker Desktop
- Close Docker Desktop completely
- Wait 10 seconds
- Restart Docker Desktop
- Wait until it's fully running (green icon in system tray)

### 2. Clear Docker System Cache
```bash
docker system prune -a --volumes
```

### 3. Try Pulling Images Individually
```bash
# Pull MySQL image
docker pull mysql:8.0

# Pull Redis image  
docker pull redis:7-alpine

# Then try docker-compose again
docker-compose up -d --build
```

### 4. Check Docker Desktop Settings
1. Open Docker Desktop
2. Go to Settings → Docker Engine
3. If you have a registry-mirrors configuration, try removing it temporarily:
   ```json
   {
     "registry-mirrors": []
   }
   ```
4. Click "Apply & Restart"

### 5. Check Network/Proxy Settings
- If using a VPN, try disconnecting it temporarily
- If using a proxy, configure it in Docker Desktop → Settings → Resources → Proxies
- Try switching to a different network (mobile hotspot, different WiFi)

### 6. Try Different DNS Servers
In Docker Desktop → Settings → Docker Engine, add:
```json
{
  "dns": ["8.8.8.8", "8.8.4.4"]
}
```

### 7. Use Alternative Image Sources (temporary workaround)
If the above doesn't work, you can manually download images or use different registries:
- Try: `docker pull registry.cn-hangzhou.aliyuncs.com/acs/mysql:8.0` (if available)
- Or build images locally if you have Dockerfiles

### 8. Reset Docker Desktop (last resort)
- Go to Docker Desktop → Troubleshoot → Reset to factory defaults
- This will remove all containers, images, and volumes

## Quick Fix Command Sequence
```bash
# 1. Stop Docker Desktop, wait, restart

# 2. Clear cache
docker system prune -a --volumes -f

# 3. Try pulling again
docker pull mysql:8.0
docker pull redis:7-alpine

# 4. If successful, run docker-compose
docker-compose up -d --build
```
