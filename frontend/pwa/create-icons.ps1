Add-Type -AssemblyName System.Drawing

foreach ($size in @(192, 512)) {
    $bitmap = New-Object System.Drawing.Bitmap($size, $size)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)

    $graphics.Clear([System.Drawing.Color]::FromArgb(23, 54, 93))
    $graphics.SmoothingMode = "AntiAlias"

    $font = New-Object System.Drawing.Font("Arial", ($size * 0.62), [System.Drawing.FontStyle]::Bold)
    $brush = [System.Drawing.Brushes]::White

    $format = New-Object System.Drawing.StringFormat
    $format.Alignment = "Center"
    $format.LineAlignment = "Center"

    $rect = New-Object System.Drawing.RectangleF(0, 0, $size, $size)

    $graphics.DrawString("E", $font, $brush, $rect, $format)

    $output = Join-Path $PSScriptRoot "icons\icon-$size.png"
    $bitmap.Save($output, [System.Drawing.Imaging.ImageFormat]::Png)

    $font.Dispose()
    $format.Dispose()
    $graphics.Dispose()
    $bitmap.Dispose()

    Write-Host "Icone creee : $output"
}