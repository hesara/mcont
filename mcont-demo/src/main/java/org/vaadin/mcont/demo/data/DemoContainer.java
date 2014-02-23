package org.vaadin.mcont.demo.data;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ExtensibleBeanContainer;

public class DemoContainer extends ExtensibleBeanContainer<String, Person> {

    public DemoContainer() {
        super(Person.class, Employee.class, Child.class);
        
        setBeanIdProperty("name");

        // optimized loop
        for (int i=1; i <= 1000; ++i) {
            addPerson(new Person("person"+i), false);
            addPerson(new Employee("employee"+i, "employer"+i), false);
            addPerson(new Child("child"+i, "rubber duck"), false);
        }
        
        fireItemSetChange();
    }
    
    /**
	 * Add a Person bean, optionally disabling filtering of the container after
	 * adding the item to better support batched operations.
	 * 
	 * @param person Person bean
	 * @param filter
	 *            true to perform filtering after adding, false to skip filtering
	 * @return
	 */
    protected BeanItem<Person> addPerson(Person person, boolean filter) {
        String itemId = resolveBeanId(person);
        if (itemId == null) {
            throw new IllegalArgumentException(
                    "Resolved identifier for a bean must not be null");
        }
		return (BeanItem<Person>) internalAddItemAtEnd(itemId,
				createBeanItem(person), filter);
    }

}
