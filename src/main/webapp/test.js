function some() {
    $(document).ready(function () {
            $("#myButtonId").click(function () {
                $("#result-place").load("converter",
                    "weight="
                    + $("#weight-field").val()
                    + "&unit="
                    + $("#unit-selector").val());
                return false;
            });
        }
    )
}