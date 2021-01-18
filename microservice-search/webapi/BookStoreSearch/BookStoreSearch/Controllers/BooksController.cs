using System.Collections.Generic;
using System.Net;
using System.Threading.Tasks;
using BookStoreSearch.Contract;
using BookStoreSearch.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ViewFeatures;

namespace BookStoreSearch.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class BooksController : ControllerBase
    {
        private readonly ISearchService<Book> _searchService;

        public BooksController(ISearchService<Book> searchService)
        {
            _searchService = searchService;
        }

        /// <summary>
        /// Gets a book from the given id.
        /// </summary>
        /// <param name="id">Book identifier.</param>
        /// <returns>200 OK with book payload if found. 204 No Content if not found.</returns>
        [HttpGet("{id}")]
        public async Task<ActionResult> Get(string id)
        {
            var item = await _searchService.Get(id);

            if (item == null)
            {
                return StatusCode((int)HttpStatusCode.NoContent);
            }

            return Ok(item);
        }

        /// <summary>
        /// Imports new books to the storage.
        /// </summary>
        /// <returns>200 with book data payload.</returns>
        [HttpPost]
        [Route("/import")]
        public async Task<ActionResult> Import([FromBody] List<Book> books)
        {
            var storedData = await _searchService.Search("*", new SearchSettings
            {
                From = 0,
                Size = 100
            });
            
            foreach(var item in storedData)
            {
                await _searchService.Delete(item.Id);
            }
            
            var results = new List<Book>();

            foreach (var book in books)
            {
                var result = await _searchService.AddOrUpdate(book);
                results.Add(result);
            }

            return Ok(results);
        }

        /// <summary>
        /// Posts a new book to the storage.
        /// </summary>
        /// <param name="book">Book payload.</param>
        /// <returns>201 Created with created book payload.</returns>
        [HttpPost]
        public async Task<ActionResult> Post([FromBody] Book book)
        {
            var result = await _searchService.AddOrUpdate(book);
            return Created("api/books/" + book.Id, result);
        }

        /// <summary>
        /// Puts a new book to the storage.
        /// </summary>
        /// <param name="id">Id of the book.</param>
        /// <param name="book">Book payload.</param>
        /// <returns>200 Ok with updated book payload.</returns>
        [HttpPut("{id}")]
        public async Task<ActionResult> Put(string id, [FromBody] Book book)
        {
            book.Id = id;
            var result = await _searchService.AddOrUpdate(book);
            return Ok(result);
        }

        /// <summary>
        /// Deletes a book from the storage.
        /// </summary>
        /// <param name="id">Id of the book.</param>
        /// <returns>200 Ok if book was found and deleted. 204 No content if book was not found.</returns>
        [HttpDelete("{id}")]
        public async Task<ActionResult> Delete(string id)
        {
            var item = await _searchService.Get(id);

            if (item == null)
            {
                return StatusCode((int)HttpStatusCode.NoContent);
            }

            await _searchService.Delete(id);

            return StatusCode((int)HttpStatusCode.OK);
        }
    }
}
