<?php

namespace PHPSTORM_META {
    override(
    // Virtual function to indicate that all SQL
    // injections will have the following replacement rules.
        sql_injection_subst(),
        map([
            '{' => "mdl_", // all `{` in injected SQL strings will be replaced with a prefix
            '}' => '',        // all `}` will be replaced with an empty string
        ]));
}
