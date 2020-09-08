# JFXTableView
Advanced JavaFx TableView with filtering and total functions.

## Description
The implementation of the filter is easy. You wrap your `TableView` with the `JFXTableView`
and add the columns which wrap `JFXTableColumn`.

There are the following column implementations:

* `JFXBooleanTableColumn`
* `JFXDoubleTableColumn`
* `JFXFloatTableColumn`
* `JFXIntegerTableColumn`
* `JFXLocalDateTableColumn`
* `JFXLocalDateTimeTableColumn`
* `JFXLocalTimeTableColumn`
* `JFXLongTableColumn`
* `JFXStringTableColumn`

You can create your own column implementation by inheriting from the abstract `JFXTableColumn` class.

You can turn off filtering on a table by passing `false` to the `setAllowFiltering` method. Example:

```
jfxTableView.setAllowFiltering(false);
```

The following types of filtering are supported (The filter types available depend on the choice of the `JFXTableColumn` implementation.):

* `Equals`
* `Not equals`
* `Greather or equals than`
* `Greather than`
* `Less or equals than`
* `Less than`
* `Start with`
* `End with`
* `Contains`
* `Not contains`
* `Regular expression`
* `Custom filtering`

## Get Started
Create `JFXTableView`:

```

```