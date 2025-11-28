@echo off
cd /d "C:\BecSales"
git add . && git commit -m "Auto-update: %date%" && git push
timeout 5