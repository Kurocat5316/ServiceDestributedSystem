<!DOCTYPE html>
<html>
<body>

<h2>Upload File</h2>
<form action="upload.php" method="POST" enctype="multipart/form-data">
<table>
        <tr><td>Access File</td> <td><input name="userfile[]" type="file" class="multi"/> </td></tr>
        <tr><td>Input File</td> <td><input name="userfile[]" type="file" class="multi"/> </td></tr>
        <tr><td>Require Date</td> <td><input name="requireDate" type="date" class="multi"/> </td></tr>
        <tr><td></td> <td> <input type="submit" name="upload" value="Upload"><input type="reset"> </td> </tr>
</table>
</form>

<h2>Check Processing Status</h2>
    <form action="passcode.php" method="POST" enctype="multipart/form-data">
    <table>
        <tr><td>passcode: <input type="number" name="passcode"></td></tr>
        <tr><td> <input type="submit" name="upload" value="Submit"> </td></tr>
    </table>
    </form>


<h2>Cancel Processing</h2>
    <form action="CancelWork.php" method="POST" enctype="multipart/form-data">
    <table>
        <tr><td>passcode: <input type="number" name="passcode"></td></tr>
        <tr><td> <input type="submit" name="upload" value="Cancel Work"> </td></tr>
    </table>
    </form>


    </body>
</html>
