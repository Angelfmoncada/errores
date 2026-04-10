param([string]$nombre = "captura")

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing
Add-Type @'
using System;
using System.Drawing;
using System.Runtime.InteropServices;
public class WC3 {
    [DllImport("user32.dll")] public static extern bool SetForegroundWindow(IntPtr hWnd);
    [DllImport("user32.dll")] public static extern bool ShowWindow(IntPtr hWnd, int n);
    [DllImport("user32.dll")] public static extern bool GetWindowRect(IntPtr hWnd, out RECT r);
    [StructLayout(LayoutKind.Sequential)]
    public struct RECT { public int Left, Top, Right, Bottom; }
}
'@ -ReferencedAssemblies System.Drawing -ErrorAction SilentlyContinue

$dir = Split-Path -Parent $MyInvocation.MyCommand.Path
$procs = Get-Process java -ErrorAction SilentlyContinue
foreach ($p in $procs) {
    if ($p.MainWindowHandle -ne [IntPtr]::Zero) {
        [WC3]::ShowWindow($p.MainWindowHandle, 9)
        [WC3]::SetForegroundWindow($p.MainWindowHandle)
        Start-Sleep -Milliseconds 600
        $rect = New-Object WC3+RECT
        [WC3]::GetWindowRect($p.MainWindowHandle, [ref]$rect)
        $w = $rect.Right - $rect.Left; $h = $rect.Bottom - $rect.Top
        if ($w -gt 0 -and $h -gt 0) {
            $bmp = New-Object System.Drawing.Bitmap($w, $h)
            $g = [System.Drawing.Graphics]::FromImage($bmp)
            $g.CopyFromScreen($rect.Left, $rect.Top, 0, 0, (New-Object System.Drawing.Size($w, $h)))
            $g.Dispose()
            $path = "$dir\$nombre.png"
            $bmp.Save($path)
            $bmp.Dispose()
            Write-Output "Saved: $path"
        }
        break
    }
}
