# Default parameter of this script
param (
    [Parameter(Mandatory = $false, Position = 0)]
    [ValidateScript({ Test-Path $_ -PathType 'Any' })]
    [string]$p,
    [Parameter(Mandatory = $false, Position = 1)]
    [string]$b,
    [Parameter(Mandatory = $false, Position = 2)]
    [string]$c
)

class Variable {
    [string] $Path
    [string] $Branch
    [string] $Comment

    Variable([string]$path, [string]$branch, [string]$comment) {
        $this.Path = $path
        $this.Branch = $branch
        $this.Comment = $comment
    }

    # Getter and Setter methods for Path property
    [string] GetPath() {
        return $this.Path
    }

    [void] SetPath([string]$value) {
        $this.Path = $value
    }

    # Getter and Setter methods for SetBranch property
    [string] GetBranch() {
        return $this.Branch
    }

    [void] SetBranch([string]$value) {
        $this.Branch = $value
    }

    # Getter and Setter methods for Comment property
    [string] GetComment() {
        return $this.Comment
    }

    [void] SetComment([string]$value) {
        $this.Comment = $value
    }
}
class Default {
    [string] $Path
    [string] $Branch
    [string] $Comment
    [string] $LoadingMessage

    Default() {
        $this.Path = $PWD.Path
        $this.Branch = $this.GetDefaultBranch()
        $this.Comment = "Auto Update"
        $this.LoadingMessage = "Loading..."
    }

    # Getter and Setter methods for Path property
    [string] GetPath() {
        return $this.Path
    }

    [void] SetPath([string]$value) {
        $this.Path = $value
    }

    # Getter and Setter methods for SetBranch property
    [string] GetBranch() {
        return $this.Branch
    }

    [void] SetBranch([string]$value) {
        $this.SetBranch = $value
    }

    # Getter and Setter methods for Comment property
    [string] GetComment() {
        return $this.Comment
    }

    [void] SetComment([string]$value) {
        $this.Comment = $value
    }

    # Getter and Setter methods for LoadingMessage property
    [string] GetLoadingMessage() {
        return $this.LoadingMessage
    }

    [void] SetLoadingMessage([string]$value) {
        $this.LoadingMessage = $value
    }
    [string] GetDefaultBranch() {
        return git rev-parse --abbrev-ref HEAD
    }
}

# Init value for better use of variables
[Default] $def = [Default]::new()
[Variable] $var = [Variable]::new($p, $b, $c)

##################################################################################
##################################################################################
##################################################################################
##################################################################################

#Setting up for first action
function Initialize-Script {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory = $false)]
        [string]$Path,
        
        [Parameter(Mandatory = $false)]
        [string]$Branch,
        
        [Parameter(Mandatory = $false)]
        [string]$Comment
    )

    begin {
        # Setting up variable
        $defaultPath = $def.GetPath()
        $defaultBranch = $def.GetBranch()
        $defaultComment = $def.GetComment()

        # Change to the desired directory
        Set-Location -Path $defaultPath

    }
    process {

        if ($null -eq $var.GetPath() -or -not $var.GetPath()) {
            Write-Host "Path is null or empty." 
            Write-host "Setting default path: $defaultPath"
            $var.SetPath($defaultPath)
            Complete-Success -lines
        }
        
        if ($null -eq $var.GetBranch() -or -not $var.GetBranch()) {
            Write-Host "Branch is null or empty." 
            Write-host "Setting default branch: $defaultBranch"
            $var.SetBranch($defaultBranch)
            Complete-Success -lines

        }
        
        if ($null -eq $var.GetComment() -or -not $var.GetComment()) {
            Write-Host "Comment is null or empty." 
            Write-host "Setting default comment: $defaultComment"
            $var.SetComment($defaultComment)
            Complete-Success -lines
            
        }
        if ($var.GetBranch() -ne $defaultBranch) {
            Write-Host "Set branch is not current branch"
            Write-Host "Set branch:" + $var.GetBranch() -ForegroundColor Blue
            Write-Host "Current branch:" +  $var.GetBranch() -ForegroundColor Green 
            Write-Host "Exiting..."
            exit
        }
    }
    end {

    }
}
function Search-Conflict {
    try {
        # Check for Git conflicts
        $gitStatus = git status --porcelain
        if ($gitStatus -match "^(?:U.|.U)") {
            throw "Git conflict detected"
        }
    }
    catch {
        Write-Host "Git conflict detected: $_"
        # Optionally, you can perform additional error handling or cleanup here
        exit 1  # Exit the script with a non-zero exit code indicating failure
    }
    finally {
        Write-Host "There is no conflict" 
    }
}
function Search-Empty {
    param (
        [Parameter(Mandatory = $true)]
        [string]$str
    )
    if ($null -eq $str -or -not $str) {
        Write-Host "Empty parameter!!!"
        Write-Host "Exiting..."
        exit
    }
    Write-Host "Parameter has been checked."
    Write-Host "Parameter: $str"
}
function Add-LoadingBar {
    [CmdletBinding()]
    Param (
        [Parameter(Mandatory = $true)]
        [string]$Message,

        [Parameter()]
        [int]$BarLength = 50,

        [Parameter()]
        [int]$DelayMilliseconds = 28
    )

    Begin {
        # Clear the console
        Clear-Host

        # Initialize variables
        $progress = 0
        $consoleBufferHeight = $Host.UI.RawUI.BufferSize.Height

        # Display the command message
        Write-Host "Command: $Message" -ForegroundColor Cyan

        # Store the current cursor position for progress and status rows
        $progressRow = [Console]::CursorTop
        $statusRow = $progressRow + 1

        # Turn off the cursor
        [Console]::CursorVisible = $false
    }

    Process {
        # Loop through the specified bar length
        for ($i = 1; $i -le $BarLength; $i++) {
            # Calculate the progress percentage and bar lengths
            $progress = $i * 100 / $BarLength
            $filledLength = [math]::Floor(($progress * $BarLength) / 100)
            $emptyLength = $BarLength - $filledLength
            $filledBar = ''
            $emptyBar = ''
            $bar = [Text.Encoding]::UTF8.GetBytes("#")
            $utfBar = [Text.Encoding]::UTF8.GetString($bar)

            # Generate the filled and empty bars
            for ($j = 0; $j -lt $emptyLength; $j++) {
                $emptyBar += ' '
            }

            for ($j = 0; $j -lt $filledLength; $j++) {
                $filledBar += $utfBar
            }

            # Update progress row
            [Console]::SetCursorPosition(0, [Math]::Min($progressRow, $consoleBufferHeight - 1))
            Write-Host -NoNewline "Processing..." -ForegroundColor Green

            # Update status row
            [Console]::SetCursorPosition(0, [Math]::Min($statusRow, $consoleBufferHeight - 1))

            # Update bar row
            $encodedOutput = [Text.Encoding]::UTF8.GetBytes("$filledBar$emptyBar")
            $Output = [Text.Encoding]::UTF8.GetString($encodedOutput)
            Write-Host -NoNewline "[$Output]" -ForegroundColor Black -BackgroundColor DarkGreen

            Write-Host "  $progress%" -ForegroundColor Yellow

            Start-Sleep -Milliseconds $DelayMilliseconds
        }
    }

    End {
        # Add a line break after the loading bar completes
        Write-Host
    }
}
# Function to check if the previous command was successful
function Complete-Success {
    param([string[]]$lines)

    if ($LASTEXITCODE -eq 0) {
        Write-Host "Success!!" -ForegroundColor Green

        foreach ($line in $lines) {
            Write-Host $line
        }
    }
    else {
        Write-Host "Error::"
        Write-Host $result
        Start-Sleep -Seconds 2
        exit    
    }
    Start-Sleep -Seconds 2
}
function Use-GitActions {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory = $true)]
        [string]$a,
        
        [Parameter(Mandatory = $false)]
        [string]$b,
        
        [Parameter(Mandatory = $false)]
        [string]$c
    )
    
    begin {
        switch ($a) {
            "Add" {
                Write-Host "Performing Add action..."
            }
            
            "Commit" {
                Write-Host "Performing Commit action..."
                if ($null -eq $c -or -not $c) {
                    Write-Host "Action: $a requires a parameter."
                    Write-Host "Please provide a valid comment with '-c' appended"
                    exit
                }
            }
            
            "Push" {
                Write-Host "Performing Push action..."
            }
            
            "Switch" {
                Write-Host "Performing Switch action..."
                if ($null -eq $b -or -not $b) {
                    Write-Host "Action: $a requires a parameter."
                    Write-Host "Please provide a valid branch with '-b' appended"
                    exit
                }
            }
            
            "Merge" {
                Write-Host "Performing Merge action..."
                if ($null -eq $b -or -not $b) {
                    Write-Host "Action: $a requires a parameter."
                    Write-Host "Please provide a valid branch with '-b' appended"
                    exit
                }
            }
            
            "PushMerged" {
                Write-Host "Performing PushMerged action..."
                if ($null -eq $b -or -not $b) {
                    Write-Host "Action: $a requires a parameter."
                    Write-Host "Please provide a valid branch with '-b' appended"
                    exit
                }
            }
            
            "SwitchBack" {
                Write-Host "Performing SwitchBack action..."
                if ($null -eq $b -or -not $b) {
                    Write-Host "Action: $a requires a parameter."
                    Write-Host "Please provide a valid branch with '-b' appended"
                    exit
                }
            }
            
            default {
                Write-Host "Invalid GitAction specified."
                return
            }
        }
    }
    process {
        switch ($a) {
            "Add" {
                Add-LoadingBar "Adding changes to staging area..."
                Search-Conflict
                $gitAddResult = git add .
                Complete-Success -lines $gitAddResult -split "`n"
            }
            
            "Commit" {
                Add-LoadingBar "Committing changes..."
                Search-Conflict
                $gitCommitResult = git commit -m $c
                Complete-Success -lines $gitCommitResult -split "`n"
            }
            
            "Push" {
                Add-LoadingBar "Pushing changes to remote repository..."
                Search-Conflict
                $gitPushResult = git push
                Complete-Success -lines $gitPushResult -split "`n"
            }
            
            "Switch" {
                Add-LoadingBar "Switching to the main branch..."
                Search-Conflict
                $gitSwitchResult = git switch main
                Complete-Success -lines $gitSwitchResult -split "`n"
            }
            
            "Merge" {
                Add-LoadingBar "Merging $Branch branch into main..."
                Search-Conflict
                $gitMergeResult = git merge $b
                Complete-Success -lines $gitMergeResult -split "`n"
            }
            
            "PushMerged" {
                Add-LoadingBar "Pushing merged changes to remote repository..."
                Search-Conflict
                $gitPushMergedResult = git push
                Complete-Success -lines $gitPushMergedResult -split "`n"
            }
            
            "SwitchBack" {
                Add-LoadingBar "Switching back to $b branch..."
                Search-Conflict
                $gitSwitchBackResult = git switch $b
                Complete-Success -lines $gitSwitchBackResult -split "`n"
            }

            "Status" {
                Add-LoadingBar "Resovling status..."
                $gitStatus = git status
                Complete-Success -lines $gitStatus -split "`n"
            }
            "Log" {
                Add-LoadingBar "Getting action logs..."
                $gitLog = git log --pretty=format:"%h - %an, %ar : %s"
                Complete-Success -lines $gitLog -split "`n"
            }
        }
    }
    end {
        Start-Sleep -Seconds 1
    }
}
function Close-Script {
    Add-LoadingBar "Finalizing Script..."
    [Console]::CursorVisible = $true
    Use-GitActions -a "Status"
    Start-Sleep -Seconds 5
    Use-GitActions -a "Log"
    Write-Host "Exiting Script..."
    Start-Sleep -Seconds 5
    exit
}


##################################################################################
##################################################################################

Initialize-Script

Use-GitActions -a "Add" -b $var.GetBranch()
Use-GitActions -a "Commit" -b $var.GetBranch() -c $var.GetComment()
Use-GitActions -a "Push" -b $var.GetBranch()
Use-GitActions -a "Switch" -b $var.GetBranch()
Use-GitActions -a "Merge" -b $var.GetBranch()
Use-GitActions -a "PushMerged" -b $var.GetBranch() 
Use-GitActions -a "SwitchBack" -b $var.GetBranch()

Close-Script