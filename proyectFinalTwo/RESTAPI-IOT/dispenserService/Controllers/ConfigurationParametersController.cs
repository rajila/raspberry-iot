using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using dispenserService.Models;
using dispenserService.Util;

namespace dispenserService.Controllers
{
    public class ConfigurationParametersController : ApiController
    {
        private dispenserServiceContext db = new dispenserServiceContext();

        [Route("api/ConfigurationParameters")]
        [HttpGet]
        // GET: api/ConfigurationParameters
        public IQueryable<ConfigurationParameter> GetConfigurations()
        {
            return db.ConfigurationParameters;
        }

        [Route("api/Dispenser")]
        [HttpGet]
        public Dispenser GetDispenser()
        {
            DateTime _time = DateTime.Now;
            Dispenser _dispenser = new Dispenser();
            _dispenser.ListConfigurationParameter = db.ConfigurationParameters.ToList();
            _dispenser.ListFoodDispenser = db.FoodDispensers.Where(s => s.CurrentDateTime.Year == _time.Year &&
                                                   s.CurrentDateTime.Month == _time.Month &&
                                                   s.CurrentDateTime.Day == _time.Day).ToList();
            _dispenser.ListWaterDispenser = db.WaterDispensers.Where(s => s.CurrentDateTime.Year == _time.Year &&
                                                   s.CurrentDateTime.Month == _time.Month &&
                                                   s.CurrentDateTime.Day == _time.Day).ToList();
            _dispenser.ListLastLog = db.Logs.OrderByDescending(s => s.CurrentDateTime).Take(1).ToList();
            return _dispenser;
        }

        // GET: api/ConfigurationParameters/5
        [Route("api/ConfigurationParameters/{id}", Name = "GetConfigurationParametersById")]
        [HttpGet]
        [ResponseType(typeof(ConfigurationParameter))]
        public async Task<IHttpActionResult> GetConfigurationParameter(int id)
        {
            ConfigurationParameter configurationParameter = await db.ConfigurationParameters.FindAsync(id);
            if (configurationParameter == null)
            {
                return NotFound();
            }

            return Ok(configurationParameter);
        }

        // PUT: api/ConfigurationParameters/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutConfigurationParameter(int id, ConfigurationParameter configurationParameter)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != configurationParameter.ConfigurationId)
            {
                return BadRequest();
            }

            db.Entry(configurationParameter).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ConfigurationParameterExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }


        // POST: api/ConfigurationParameters
        [Route("api/ConfigurationParameters")]
        [HttpPost]
        [ResponseType(typeof(ConfigurationParameter))]
        public async Task<IHttpActionResult> PostConfigurationParameter(ConfigurationParameter configurationParameter)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            ConfigurationParameter _data = db.ConfigurationParameters.FirstOrDefault();
            DateTime _time = DateTime.Now;

            if (_data == null)
            {
                configurationParameter.CurrentDateTime = _time;

                db.ConfigurationParameters.Add(configurationParameter);
                await db.SaveChangesAsync();
                _data = configurationParameter;
            }
            else {
                _data.AmountDailyFood = configurationParameter.AmountDailyFood;
                _data.AmountDailyWater = configurationParameter.AmountDailyWater;
                _data.AmountBowlFoodWater = configurationParameter.AmountBowlFoodWater;
                _data.CurrentDateTime = _time;
                db.Entry(_data).State = EntityState.Modified;
                try
                {
                    await db.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!ConfigurationParameterExists(_data.ConfigurationId))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
            }

            return CreatedAtRoute("GetConfigurationParametersById", new { id = _data.ConfigurationId }, _data);
        }
        /*
        // DELETE: api/ConfigurationParameters/5
        [ResponseType(typeof(ConfigurationParameter))]
        public async Task<IHttpActionResult> DeleteConfigurationParameter(int id)
        {
            ConfigurationParameter configurationParameter = await db.Configurations.FindAsync(id);
            if (configurationParameter == null)
            {
                return NotFound();
            }

            db.Configurations.Remove(configurationParameter);
            await db.SaveChangesAsync();

            return Ok(configurationParameter);
        }*/

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ConfigurationParameterExists(int id)
        {
            return db.ConfigurationParameters.Count(e => e.ConfigurationId == id) > 0;
        }
    }
}