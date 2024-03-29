package org.hikit.common.response

import org.hikit.common.response.ControllerPagination.checkSkipLim
import org.junit.Assert.*
import org.junit.Test
import kotlin.test.assertFailsWith

class ControllerPaginationTest {

    @Test
    fun `shall return correct number of pages`() {
        assertEquals(1, ControllerPagination.getTotalPages(0, 1))
        assertEquals(10, ControllerPagination.getTotalPages(10, 1))
        assertEquals(2, ControllerPagination.getTotalPages(10, 5))
        assertEquals(4, ControllerPagination.getTotalPages(10, 3))
        assertEquals(167, ControllerPagination.getTotalPages(1000, 6))
    }

    @Test
    fun `shall return correct page number`() {
        assertEquals(1, ControllerPagination.getCurrentPage(0, 150))
        assertEquals(1, ControllerPagination.getCurrentPage(0, 5))
        assertEquals(2, ControllerPagination.getCurrentPage(11, 10))
        assertEquals(3, ControllerPagination.getCurrentPage(20, 10))
        assertEquals(3, ControllerPagination.getCurrentPage(29, 10))
    }


    @Test
    fun `shall throw exception if limit equals to zero`() {
        assertFailsWith<IllegalArgumentException> {
            checkSkipLim(20, 0)
        }
    }

    @Test
    fun `shall throw exception if limit equals to zero and skip zero`() {
        assertFailsWith<IllegalArgumentException> {
            checkSkipLim(0, 0)
        }
    }

    @Test
    fun `shall throw exception if skip lower than zero`() {
        assertFailsWith<IllegalArgumentException> {
            checkSkipLim(-1, 0)
        }
    }

}