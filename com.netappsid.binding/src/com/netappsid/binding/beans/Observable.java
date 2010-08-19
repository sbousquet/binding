/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.netappsid.binding.beans;

import java.beans.PropertyChangeListener;

/**
 * Describes objects that provide bound properties as specified in the
 * <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java
 * Bean Specification</a>.
 * This interface is primarily intended to ensure compile-time safety
 * for beans that shall be observed by a {@link BeanAdapter} or
 * {@link PropertyAdapter}. However, these classes can observe beans
 * that follow the Bean specification for bound properties
 * - even if the beans don't implement this Observable interface.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 *
 * @see     PropertyChangeListener
 * @see     java.beans.PropertyChangeEvent
 * @see     java.beans.PropertyChangeSupport
 * @see     BeanAdapter
 * @see     PropertyAdapter
 */
public interface Observable {


    /**
     * Adds the given PropertyChangeListener to the listener list.
     * The listener is registered for all bound properties of this class.
     *
     * @param listener      the PropertyChangeListener to be added
     *
     * @see #removePropertyChangeListener(PropertyChangeListener)
     */
    void addPropertyChangeListener(PropertyChangeListener listener);


    /**
     * Removes the given PropertyChangeListener from the listener list.
     * This method should be used to remove PropertyChangeListeners that were
     * registered for all bound properties of this class.
     *
     * @param listener      the PropertyChangeListener to be removed
     *
     * @see #addPropertyChangeListener(PropertyChangeListener)
     */
    void removePropertyChangeListener(PropertyChangeListener listener);


}